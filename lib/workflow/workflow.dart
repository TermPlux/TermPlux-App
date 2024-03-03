import 'dart:io';
import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart';
import 'package:retry/retry.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:xterm/xterm.dart';
import 'package:flutter_pty/flutter_pty.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:wakelock_plus/wakelock_plus.dart';

import '../value/default.dart';
import '../value/global.dart';
import '../terminal/term_pty.dart';
import '../terminal/virtual_keyboard.dart';
import '../utils/util.dart';

class Workflow {
  static Future<void> grantPermissions() async {
    Permission.storage.request();
    //Permission.manageExternalStorage.request();
  }

  static Future<void> setupBootstrap() async {
    //用来共享数据文件的文件夹
    Util.createDirFromString("${Global.dataPath}/share");
    //挂载到/dev/shm的文件夹
    Util.createDirFromString("${Global.dataPath}/tmp");
    //给proot的tmp文件夹，虽然我不知道为什么proot要这个
    Util.createDirFromString("${Global.dataPath}/proot_tmp");
    //给pulseaudio的tmp文件夹
    Util.createDirFromString("${Global.dataPath}/pulseaudio_tmp");
    //解压后得到bin文件夹和libexec文件夹
    //bin存放了proot, pulseaudio, tar等
    //libexec存放了proot loader
    await Util.copyAsset(
      "assets/assets.zip",
      "${Global.dataPath}/assets.zip",
    );
    //patch.tar.gz存放了tiny文件夹
    //里面是一些补丁，会被挂载到~/.local/share/tiny
    await Util.copyAsset(
      "assets/patch.tar.gz",
      "${Global.dataPath}/patch.tar.gz",
    );
    //dddd
    await Util.copyAsset(
      "assets/busybox",
      "${Global.dataPath}/busybox",
    );
    await Util.execute("""
export DATA_DIR=${Global.dataPath}
cd \$DATA_DIR
chmod +x busybox
\$DATA_DIR/busybox unzip -o assets.zip
chmod -R +x bin/*
chmod -R +x libexec/proot/*
chmod 1777 tmp
ln -sf \$DATA_DIR/busybox \$DATA_DIR/bin/xz
ln -sf \$DATA_DIR/busybox \$DATA_DIR/bin/gzip
\$DATA_DIR/bin/tar zxf patch.tar.gz
\$DATA_DIR/busybox rm -rf assets.zip patch.tar.gz
""");
  }

  //初次启动要做的事情
  static Future<void> initForFirstTime() async {
    //首先设置bootstrap
    Global.updateText.value = "正在安装引导包";
    await setupBootstrap();

    Global.updateText.value = "正在复制容器系统";
    //存放容器的文件夹0和存放硬链接的文件夹.l2s
    Util.createDirFromString("${Global.dataPath}/containers/0/.l2s");
    //这个是容器rootfs，被split命令分成了xa*，放在assets里
    //首次启动，就用这个，别让用户另选了
    for (String name
        in jsonDecode(await rootBundle.loadString('AssetManifest.json'))
            .keys
            .where((String e) => e.startsWith("assets/xa"))
            .map((String e) => e.split("/").last)
            .toList()) {
      await Util.copyAsset("assets/$name", "${Global.dataPath}/$name");
    }
    //-J
    Global.updateText.value = "正在安装容器系统";
    await Util.execute("""
export DATA_DIR=${Global.dataPath}
export CONTAINER_DIR=\$DATA_DIR/containers/0
export EXTRA_OPT=""
cd \$DATA_DIR
export PATH=\$DATA_DIR/bin:\$PATH
export PROOT_TMP_DIR=\$DATA_DIR/proot_tmp
export PROOT_LOADER=\$DATA_DIR/libexec/proot/loader
export PROOT_LOADER_32=\$DATA_DIR/libexec/proot/loader32
#export PROOT_L2S_DIR=\$CONTAINER_DIR/.l2s
\$DATA_DIR/bin/proot --link2symlink sh -c "cat xa* | \$DATA_DIR/bin/tar x -J --delay-directory-restore --preserve-permissions -v -C containers/0"
#Script from proot-distro
chmod u+rw "\$CONTAINER_DIR/etc/passwd" "\$CONTAINER_DIR/etc/shadow" "\$CONTAINER_DIR/etc/group" "\$CONTAINER_DIR/etc/gshadow"
echo "aid_\$(id -un):x:\$(id -u):\$(id -g):Termux:/:/sbin/nologin" >> "\$CONTAINER_DIR/etc/passwd"
echo "aid_\$(id -un):*:18446:0:99999:7:::" >> "\$CONTAINER_DIR/etc/shadow"
id -Gn | tr ' ' '\\n' > tmp1
id -G | tr ' ' '\\n' > tmp2
\$DATA_DIR/busybox paste tmp1 tmp2 > tmp3
local group_name group_id
cat tmp3 | while read -r group_name group_id; do
	echo "aid_\${group_name}:x:\${group_id}:root,aid_\$(id -un)" >> "\$CONTAINER_DIR/etc/group"
	if [ -f "\$CONTAINER_DIR/etc/gshadow" ]; then
		echo "aid_\${group_name}:*::root,aid_\$(id -un)" >> "\$CONTAINER_DIR/etc/gshadow"
	fi
done
\$DATA_DIR/busybox rm -rf xa* tmp1 tmp2 tmp3
""");
    //一些数据初始化
    //$DATA_DIR是数据文件夹, $CONTAINER_DIR是容器根目录
    await Global.prefs.setStringList("containersInfo", [
      """{
"name":"Debian Bookworm",
"boot":"${Default.boot}",
"vnc":"startnovnc &",
"vncUrl":"http://localhost:36082/vnc.html?host=localhost&port=36082&autoconnect=true&resize=remote&password=12345678",
"commands":${jsonEncode(Default.commands)}
}"""
    ]);
    Global.updateText.value = "安装完成";
  }

  static Future<void> initData() async {
    Global.dataPath = (await getApplicationSupportDirectory()).path;

    Global.termPtys = {};

    Global.keyboard = VirtualKeyboard(defaultInputHandler);

    Global.prefs = await SharedPreferences.getInstance();

    //如果没有这个key，说明是初次启动
    if (!Global.prefs.containsKey("defaultContainer")) {
      await initForFirstTime();
    }
    Global.currentContainer = Util.getGlobal("defaultContainer") as int;

    //是否需要重新安装引导包?
    if (Util.getGlobal("reinstallBootstrap")) {
      Global.updateText.value = "正在重新安装引导包";
      await setupBootstrap();
      Global.prefs.setBool("reinstallBootstrap", false);
    }

    Global.termFontScale.value = Util.getGlobal("termFontScale") as double;

    Global.controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted);

    //设置屏幕常亮
    WakelockPlus.toggle(enable: Util.getGlobal("wakelock"));
  }

  static Future<void> initTerminalForCurrent() async {
    if (!Global.termPtys.containsKey(Global.currentContainer)) {
      Global.termPtys[Global.currentContainer] = TermPty();
    }
  }

  static Future<void> setupAudio() async {
    Global.audioPty?.kill();
    Global.audioPty = Pty.start("/system/bin/sh");
    Global.audioPty!.write(const Utf8Encoder().convert("""
export DATA_DIR=${Global.dataPath}
\$DATA_DIR/busybox sed "s/4713/${Util.getGlobal("defaultAudioPort") as int}/g" \$DATA_DIR/bin/pulseaudio.conf > \$DATA_DIR/bin/pulseaudio.conf.tmp
rm -rf \$DATA_DIR/pulseaudio_tmp/*
TMPDIR=\$DATA_DIR/pulseaudio_tmp HOME=\$DATA_DIR/pulseaudio_tmp XDG_CONFIG_HOME=\$DATA_DIR/pulseaudio_tmp LD_LIBRARY_PATH=\$DATA_DIR/bin \$DATA_DIR/bin/pulseaudio -F \$DATA_DIR/bin/pulseaudio.conf.tmp
exit
"""));
    await Global.audioPty?.exitCode;
  }

  static Future<void> launchCurrentContainer() async {
    String box86BinPath = "";
    String box64BinPath = "";
    String box86LibraryPath = "";
    String box64LibraryPath = "";
    String extraMount = ""; //mount options and other proot options
    String extraOpt = "";
    if (Util.getGlobal("getifaddrsBridge")) {
      Util.execute(
          "${Global.dataPath}/bin/getifaddrs_bridge_server ${Global.dataPath}/containers/${Global.currentContainer}/tmp/.getifaddrs-bridge");
      extraOpt +=
          "LD_PRELOAD=/home/tiny/.local/share/tiny/extra/getifaddrs_bridge_client_lib.so ";
    }
    if (Util.getGlobal("isHidpiEnabled")) {
      extraOpt += "${Util.getGlobal("defaultHidpiOpt")} ";
    }
    if (Util.getGlobal("uos")) {
      extraMount +=
          "--mount=\$DATA_DIR/tiny/wechat/uos-lsb:/etc/lsb-release --mount=\$DATA_DIR/tiny/wechat/uos-release:/usr/lib/os-release ";
      extraMount +=
          "--mount=\$DATA_DIR/tiny/wechat/license/var/uos:/var/uos --mount=\$DATA_DIR/tiny/wechat/license/var/lib/uos-license:/var/lib/uos-license ";
    }
    if (Util.getGlobal("virgl")) {
      Util.execute("""
export DATA_DIR=${Global.dataPath}
export CONTAINER_DIR=\$DATA_DIR/containers/${Global.currentContainer}
${Global.dataPath}/bin/virgl_test_server ${Util.getGlobal("defaultVirglCommand")}""");
      extraOpt += "${Util.getGlobal("defaultVirglOpt")} ";
    }
    if (Util.getGlobal("isBoxEnabled")) {
      Global.wasBoxEnabled = true;
      extraMount +=
          "--x86=/home/tiny/.local/bin/box86 --x64=/home/tiny/.local/bin/box64 ";
      extraMount +=
          "--mount=\$DATA_DIR/tiny/cross/box86:/home/tiny/.local/bin/box86 --mount=\$DATA_DIR/tiny/cross/box64:/home/tiny/.local/bin/box64 ";
      extraOpt += "BOX86_NOBANNER=1 BOX64_NOBANNER=1 ";
    }
    if (Util.getGlobal("isWineEnabled")) {
      Global.wasWineEnabled = true;
      box86BinPath += "/home/tiny/.local/share/tiny/cross/wine/bin:";
      box64BinPath += "/home/tiny/.local/share/tiny/cross/wine/bin:";
      box86LibraryPath +=
          "/home/tiny/.local/share/tiny/cross/wine/lib/wine/i386-unix:";
      box64LibraryPath +=
          "/home/tiny/.local/share/tiny/cross/wine/lib/wine/x86_64-unix:";
      extraMount += "--wine=/home/tiny/.local/bin/wine64 ";
      extraMount +=
          "--mount=\$DATA_DIR/tiny/cross/wine.desktop:/usr/share/applications/wine.desktop ";
      //extraMount += "--mount=\$DATA_DIR/tiny/cross/winetricks:/home/tiny/.local/bin/winetricks --mount=\$DATA_DIR/tiny/cross/winetricks.desktop:/usr/share/applications/winetricks.desktop ";
    }
    if (Global.wasBoxEnabled) {
      extraOpt +=
          "BOX86_PATH=$box86BinPath/home/tiny/.local/share/tiny/cross/bin ";
      extraOpt +=
          "BOX64_PATH=$box64BinPath/home/tiny/.local/share/tiny/cross/bin ";
      extraOpt +=
          "BOX86_LD_LIBRARY_PATH=$box86LibraryPath/home/tiny/.local/share/tiny/cross/x86lib ";
      extraOpt +=
          "BOX64_LD_LIBRARY_PATH=$box64LibraryPath/home/tiny/.local/share/tiny/cross/x64lib ";
    }
    Util.termWrite("""
export DATA_DIR=${Global.dataPath}
export CONTAINER_DIR=\$DATA_DIR/containers/${Global.currentContainer}
export EXTRA_MOUNT="$extraMount"
export EXTRA_OPT="$extraOpt"
#export PROOT_L2S_DIR=\$DATA_DIR/containers/0/.l2s
cd \$DATA_DIR
export PROOT_TMP_DIR=\$DATA_DIR/proot_tmp
export PROOT_LOADER=\$DATA_DIR/libexec/proot/loader
export PROOT_LOADER_32=\$DATA_DIR/libexec/proot/loader32
${Util.getCurrentProp("boot")}
${Util.getCurrentProp("vnc")}
clear""");
  }

  static Future<void> waitForConnection() async {
    await retry(
      // Make a GET request
      () => get(
        Uri.parse(
          Util.getCurrentProp("vncUrl"),
        ),
      ).timeout(
        const Duration(milliseconds: 250),
      ),
      // Retry on SocketException or TimeoutException
      retryIf: (e) => e is SocketException || e is TimeoutException,
    );
  }

  static Future<void> loadDesktop() async {
    Global.controller.loadRequest(Uri.parse(Util.getCurrentProp("vncUrl")));
  }

  static Future<void> launchAvnc() async {
    await Default.avncChannel.invokeMethod("launchUsingUri", {"vncUri": Util.getCurrentProp("vncUri") as String});
  }


  static Future<void> workflow() async {
    grantPermissions();
    await initData();
    await initTerminalForCurrent();
    setupAudio();
    launchCurrentContainer();

    waitForConnection().then((value) {
      loadDesktop();
      launchAvnc();
    });
  }
}
