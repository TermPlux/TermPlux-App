import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter_pty/flutter_pty.dart';

import '../value/global.dart';

class Util {
  static Future<void> copyAsset(String src, String dst) async {
    await File(dst).writeAsBytes(
      (await rootBundle.load(src)).buffer.asUint8List(),
    );
  }

  static Future<void> copyAsset2(String src, String dst) async {
    ByteData data = await rootBundle.load(src);
    await File(dst).writeAsBytes(
      data.buffer.asUint8List(data.offsetInBytes, data.lengthInBytes),
    );
  }

  static void createDirFromString(String dir) {
    Directory.fromRawPath(const Utf8Encoder().convert(dir))
        .createSync(recursive: true);
  }

  static Future<int> execute(String str) async {
    Pty pty = Pty.start("/system/bin/sh");
    pty.write(const Utf8Encoder().convert("$str\nexit \$?\n"));
    return await pty.exitCode;
  }

  static void termWrite(String str) {
    Global.termPtys[Global.currentContainer]!.pty
        .write(const Utf8Encoder().convert("$str\n"));
  }

  //所有key
  //int defaultContainer = 0: 默认启动第0个容器
  //int defaultAudioPort = 4718: 默认pulseaudio端口(为了避免和其它软件冲突改成4718了，原默认4713)
  //bool autoLaunchVnc = true: 是否自动启动VNC并跳转
  //String lastDate: 上次启动软件的日期，yyyy-MM-dd
  //int adsWatchedToday: 今日视频广告观看数量
  //int adsWatchedTotal: 视频广告观看数量
  //bool isBannerAdsClosed = false
  //bool isTerminalWriteEnabled = false
  //bool isTerminalCommandsEnabled = false
  //int termMaxLines = 4095 终端最大行数
  //double termFontScale = 1 终端字体大小
  //int vip = 0 用户等级，vip免广告，你要改吗？(ToT)
  //bool isStickyKey = true 终端ctrl, shift, alt键是否粘滞
  //String defaultFFmpegCommand 默认推流命令
  //String defaultVirglCommand 默认virgl参数
  //String defaultVirglOpt 默认virgl环境变量
  //bool reinstallBootstrap = false 下次启动是否重装引导包
  //bool getifaddrsBridge = false 下次启动是否桥接getifaddrs
  //bool uos = false 下次启动是否伪装UOS
  //bool isBoxEnabled = false 下次启动是否开启box86/box64
  //bool isWineEnabled = false 下次启动是否开启wine
  //bool virgl = false 下次启动是否启用virgl
  //bool wakelock = false 屏幕常亮
  //bool isHidpiEnabled = false 是否开启高分辨率
  //String defaultHidpiOpt 默认HiDPI环境变量
  //? int bootstrapVersion: 启动包版本
  //String[] containersInfo: 所有容器信息(json)
  //{name, boot:"\$DATA_DIR/bin/proot ...", vnc:"startnovnc", vncUrl:"...", commands:[{name:"更新和升级", command:"apt update -y && apt upgrade -y"},
  // bind:[{name:"U盘", src:"/storage/xxxx", dst:"/media/meow"}]...]}
  //String[] adsBonus: 观看广告获取的奖励(json)
  //{name: "xxx", amount: xxx}
  //TODO: 这么写还是不对劲，有空改成类试试？
  static dynamic getGlobal(String key) {
    bool b = Global.prefs.containsKey(key);
    switch (key) {
      case "defaultContainer":
        return b
            ? Global.prefs.getInt(key)!
            : (value) {
          Global.prefs.setInt(key, value);
          return value;
        }(0);
      case "defaultAudioPort":
        return b
            ? Global.prefs.getInt(key)!
            : (value) {
          Global.prefs.setInt(key, value);
          return value;
        }(4718);
      case "lastDate":
        return b
            ? Global.prefs.getString(key)!
            : (value) {
          Global.prefs.setString(key, value);
          return value;
        }("1970-01-01");
      // case "adsWatchedToday":
      //   return b
      //       ? Global.prefs.getInt(key)!
      //       : (value) {
      //     Global.prefs.setInt(key, value);
      //     return value;
      //   }(0);
      // case "adsWatchedTotal":
      //   return b
      //       ? Global.prefs.getInt(key)!
      //       : (value) {
      //     Global.prefs.setInt(key, value);
      //     return value;
      //   }(0);
      // case "isBannerAdsClosed":
      //   return b
      //       ? Global.prefs.getBool(key)!
      //       : (value) {
      //     Global.prefs.setBool(key, value);
      //     return value;
      //   }(false);
      // case "isTerminalWriteEnabled":
      //   return b
      //       ? Global.prefs.getBool(key)!
      //       : (value) {
      //     Global.prefs.setBool(key, value);
      //     return value;
      //   }(false);
      // case "isTerminalCommandsEnabled":
      //   return b
      //       ? Global.prefs.getBool(key)!
      //       : (value) {
      //     Global.prefs.setBool(key, value);
      //     return value;
      //   }(false);
      case "termMaxLines":
        return b
            ? Global.prefs.getInt(key)!
            : (value) {
          Global.prefs.setInt(key, value);
          return value;
        }(4095);
      case "termFontScale":
        return b
            ? Global.prefs.getDouble(key)!
            : (value) {
          Global.prefs.setDouble(key, value);
          return value;
        }(1.0);
      // case "vip":
      //   return b
      //       ? Global.prefs.getInt(key)!
      //       : (value) {
      //     Global.prefs.setInt(key, value);
      //     return value;
      //   }(0);
      case "isStickyKey":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(true);
      case "reinstallBootstrap":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "getifaddrsBridge":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "uos":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "isBoxEnabled":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "isWineEnabled":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "virgl":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "wakelock":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "isHidpiEnabled":
        return b
            ? Global.prefs.getBool(key)!
            : (value) {
          Global.prefs.setBool(key, value);
          return value;
        }(false);
      case "defaultFFmpegCommand":
        return b
            ? Global.prefs.getString(key)!
            : (value) {
          Global.prefs.setString(key, value);
          return value;
        }("-hide_banner -an -max_delay 1000000 -r 30 -f android_camera -camera_index 0 -i 0:0 -vf scale=iw/2:-1 -rtsp_transport udp -f rtsp rtsp://127.0.0.1:8554/stream");
      case "defaultVirglCommand":
        return b
            ? Global.prefs.getString(key)!
            : (value) {
          Global.prefs.setString(key, value);
          return value;
        }("--socket-path=\$CONTAINER_DIR/tmp/.virgl_test");
      case "defaultVirglOpt":
        return b
            ? Global.prefs.getString(key)!
            : (value) {
          Global.prefs.setString(key, value);
          return value;
        }("GALLIUM_DRIVER=virpipe MESA_GL_VERSION_OVERRIDE=4.0");
      case "defaultHidpiOpt":
        return b
            ? Global.prefs.getString(key)!
            : (value) {
          Global.prefs.setString(key, value);
          return value;
        }("GDK_SCALE=2 QT_FONT_DPI=192");
      case "containersInfo":
        return Global.prefs.getStringList(key)!;
      // case "adsBonus":
      //   return b
      //       ? Global.prefs.getStringList(key)!
      //       : (value) {
      //     Global.prefs.setStringList(key, value);
      //     return value;
      //   }([].cast<String>());
    }
  }

  static dynamic getCurrentProp(String key) {
    return jsonDecode(
      Util.getGlobal("containersInfo")[Global.currentContainer],
    )[key];
  }

  //用来设置name, boot, vnc, vncUrl等
  static Future<void> setCurrentProp(String key, dynamic value) async {
    await Global.prefs.setStringList(
      "containersInfo",
      Util.getGlobal("containersInfo")
        ..setAll(
          Global.currentContainer,
          [
            jsonEncode(
              (jsonDecode(Util.getGlobal("containersInfo")[Global.currentContainer]))
                ..update(key, (v) => value),
            )
          ],
        ),
    );
  }

  //由getRandomBonus返回的数据
  static Future<void> applyBonus(Map<String, dynamic> bonus) async {
    bool flag = false;
    List<String> ret = Util.getGlobal("adsBonus")
        .map((e) {
      Map<String, dynamic> item = jsonDecode(e);
      return (item["name"] == bonus["name"])
          ? jsonEncode(item
        ..update("amount", (v) {
          flag = true;
          return v + bonus["amount"];
        }))
          : e;
    })
        .toList()
        .cast<String>();
    if (!flag) {
      ret.add("""{"name": "${bonus["name"]}", "amount": ${bonus["amount"]}}""");
    }
    await Global.prefs.setStringList("adsBonus", ret);
  }

  //限定字符串在min和max之间, 给文本框的validator
  static String? validateBetween(
      String? value, int min, int max, Function opr) {
    if (value == null || value.isEmpty) {
      return "请输入数字";
    }
    int? parsedValue = int.tryParse(value);
    if (parsedValue == null) {
      return "请输入有效的数字";
    }
    if (parsedValue < min || parsedValue > max) {
      return "请输入$min到$max之间的数字";
    }
    opr();
    return null;
  }
}