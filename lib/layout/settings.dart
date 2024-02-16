import 'dart:convert';
import 'dart:io';

import 'package:clipboard/clipboard.dart';
import 'package:ffmpeg_kit_flutter_full_gpl/ffmpeg_kit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_pty/flutter_pty.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:wakelock_plus/wakelock_plus.dart';

import '../value/default.dart';
import '../value/global.dart';
import '../utils/util.dart';

class SettingPage extends StatefulWidget {
  const SettingPage({super.key});

  @override
  State<SettingPage> createState() => _SettingPageState();
}

class _SettingPageState extends State<SettingPage> {
  final List<bool> _expandState = [
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false
  ];

  @override
  Widget build(BuildContext context) {
    return ExpansionPanelList(
      elevation: 1,
      expandedHeaderPadding: const EdgeInsets.all(0),
      expansionCallback: (panelIndex, isExpanded) {
        setState(() {
          _expandState[panelIndex] = isExpanded;
        });
      },
      children: [
        ExpansionPanel(
          isExpanded: _expandState[0],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(
                title: Text("高级设置"), subtitle: Text("修改后重启生效"));
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                OutlinedButton(
                  style: Default.commandButtonStyle,
                  child: const Text("重置启动命令"),
                  onPressed: () {
                    showDialog(
                      context: context,
                      builder: (context) {
                        return AlertDialog(
                          title: const Text("注意"),
                          content: const Text("是否重置启动命令？"),
                          actions: [
                            TextButton(
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                                child: const Text("取消")),
                            TextButton(
                              onPressed: () async {
                                await Util.setCurrentProp("boot", Default.boot);
                                Global.bootTextChange.value =
                                    !Global.bootTextChange.value;
                                if (!context.mounted) return;
                                Navigator.of(context).pop();
                              },
                              child: const Text("是"),
                            ),
                          ],
                        );
                      },
                    );
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getCurrentProp("name"),
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(), labelText: "容器名称"),
                  onChanged: (value) async {
                    await Util.setCurrentProp("name", value);
                    //setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                ValueListenableBuilder(
                  valueListenable: Global.bootTextChange,
                  builder: (context, v, child) {
                    return TextFormField(
                      maxLines: null,
                      initialValue: Util.getCurrentProp("boot"),
                      decoration: const InputDecoration(
                          border: OutlineInputBorder(), labelText: "启动命令"),
                      onChanged: (value) async {
                        await Util.setCurrentProp("boot", value);
                      },
                    );
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getCurrentProp("vnc"),
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(), labelText: "noVNC启动命令"),
                  onChanged: (value) async {
                    await Util.setCurrentProp("vnc", value);
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                //const Divider(height: 2, indent: 8, endIndent: 8),
                //SizedBox.fromSize(size: const Size.square(16)),
                //const Text(
                //    "你可以在当前所有同一网络下的设备（如：连接同一路由器的手机，电脑等）里使用小小电脑。\n\n将下面的链接复制到其他设备，在其他设备上把链接localhost字样改为当前设备的IP地址（可以通过快捷指令查看，格式类似192.168.x.x），然后使用浏览器打开链接即可。"),
                //SizedBox.fromSize(size: const Size.square(16)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getCurrentProp('vncUrl'),
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "noVNC地址",
                  ),
                  onChanged: (value) async {
                    await Util.setCurrentProp("vncUrl", value);
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
              ],
            ),
          ),
        ),
        ExpansionPanel(
          isExpanded: _expandState[1],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(
                title: Text("全局设置"), subtitle: Text("在这里关广告、开启终端编辑"));
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                TextFormField(
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  initialValue:
                      (Util.getGlobal("termMaxLines") as int).toString(),
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "终端最大行数(重启软件生效)",
                  ),
                  keyboardType: TextInputType.number,
                  validator: (value) {
                    return Util.validateBetween(value, 1024, 2147483647,
                        () async {
                      await Global.prefs
                          .setInt("termMaxLines", int.parse(value!));
                    });
                  },
                ),
                SizedBox.fromSize(size: const Size.square(16)),
                TextFormField(
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  initialValue:
                      (Util.getGlobal("defaultAudioPort") as int).toString(),
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: "pulseaudio接收端口"),
                  keyboardType: TextInputType.number,
                  validator: (value) {
                    return Util.validateBetween(value, 0, 65535, () async {
                      await Global.prefs
                          .setInt("defaultAudioPort", int.parse(value!));
                    });
                  },
                ),
                SizedBox.fromSize(size: const Size.square(16)),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("终端粘滞键"),
                  value: Util.getGlobal("isStickyKey") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("isStickyKey", value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("屏幕常亮"),
                  value: Util.getGlobal("wakelock") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("wakelock", value);
                    WakelockPlus.toggle(enable: value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                const Divider(height: 2, indent: 8, endIndent: 8),
                SizedBox.fromSize(size: const Size.square(16)),
                const Text("以下选项修改后将在下次启动软件时生效。"),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("重新安装引导包"),
                  value: Util.getGlobal("reinstallBootstrap") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("reinstallBootstrap", value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("getifaddrs桥接"),
                  subtitle: const Text("修复安卓13设备getifaddrs无权限"),
                  value: Util.getGlobal("getifaddrsBridge") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("getifaddrsBridge", value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("伪装系统为UOS"),
                  subtitle: const Text("修复UOS微信无法启动"),
                  value: Util.getGlobal("uos") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("uos", value);
                    setState(() {});
                  },
                ),
              ],
            ),
          ),
        ),
        ExpansionPanel(
          isExpanded: _expandState[2],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(
              title: Text("相机推流"),
              subtitle: Text("实验性功能"),
            );
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                const Text(
                    "成功启动推流后可以点击快捷指令\"拉流测试\"并前往图形界面查看效果。\n注意这并不能为系统创建一个虚拟相机；\n另外使用相机是高耗电行为，不用时需及时关闭。"),
                const SizedBox.square(dimension: 16),
                Wrap(
                  alignment: WrapAlignment.center,
                  spacing: 4.0,
                  runSpacing: 4.0,
                  children: [
                    OutlinedButton(
                      style: Default.commandButtonStyle,
                      child: const Text("申请相机权限"),
                      onPressed: () {
                        Permission.camera.request();
                      },
                    ),
                    OutlinedButton(
                      style: Default.commandButtonStyle,
                      child: const Text("申请麦克风权限"),
                      onPressed: () {
                        Permission.microphone.request();
                      },
                    ),
                    OutlinedButton(
                      style: Default.commandButtonStyle,
                      child: const Text("查看输出"),
                      onPressed: () {
                        if (Global.streamingOutput == "") {
                          ScaffoldMessenger.of(context).hideCurrentSnackBar();
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text("无输出"),
                            ),
                          );
                          return;
                        }
                        showDialog(
                          context: context,
                          builder: (context) {
                            return AlertDialog(
                              content: SingleChildScrollView(
                                child: Text(Global.streamingOutput),
                              ),
                              actions: [
                                TextButton(
                                  onPressed: () {
                                    FlutterClipboard.copy(
                                            Global.streamingOutput)
                                        .then((value) {
                                      ScaffoldMessenger.of(context)
                                          .hideCurrentSnackBar();
                                      ScaffoldMessenger.of(context)
                                          .showSnackBar(
                                        const SnackBar(
                                          content: Text("已复制"),
                                        ),
                                      );
                                    });
                                    Navigator.of(context).pop();
                                  },
                                  child: const Text("复制"),
                                ),
                                TextButton(
                                  onPressed: () {
                                    Navigator.of(context).pop();
                                  },
                                  child: const Text("取消"),
                                ),
                              ],
                            );
                          },
                        );
                      },
                    ),
                  ],
                ),
                const SizedBox.square(dimension: 16),
                TextFormField(
                  maxLines: null,
                  initialValue:
                      Util.getGlobal("defaultFFmpegCommand") as String,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: "ffmpeg推流命令",
                  ),
                  onChanged: (value) async {
                    await Global.prefs.setString("defaultFFmpegCommand", value);
                  },
                ),
                const SizedBox.square(dimension: 16),
                SwitchListTile(
                  title: const Text("启动推流服务器"),
                  subtitle: const Text("mediamtx"),
                  value: Global.isStreamServerStarted,
                  onChanged: (value) {
                    switch (value) {
                      case true:
                        {
                          Global.streamServerPty = Pty.start("/system/bin/sh");
                          Global.streamServerPty.write(const Utf8Encoder().convert(
                              "${Global.dataPath}/bin/mediamtx ${Global.dataPath}/bin/mediamtx.yml\nexit\n"));
                          Global.streamServerPty.exitCode.then((value) {
                            Global.isStreamServerStarted = false;
                            setState(() {});
                          });
                        }
                        break;
                      case false:
                        {
                          Global.streamServerPty
                              .write(const Utf8Encoder().convert("\x03exit\n"));
                        }
                        break;
                    }
                    Global.isStreamServerStarted = value;
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("启动推流"),
                  value: Global.isStreaming,
                  onChanged: (value) {
                    switch (value) {
                      case true:
                        {
                          FFmpegKit.execute(
                                  Util.getGlobal("defaultFFmpegCommand")
                                      as String)
                              .then((session) {
                            session.getOutput().then((value) async {
                              Global.isStreaming = false;
                              Global.streamingOutput = value ?? "";
                              setState(() {});
                            });
                          });
                        }
                        break;
                      case false:
                        {
                          FFmpegKit.cancel();
                        }
                        break;
                    }
                    Global.isStreaming = value;
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8))
              ],
            ),
          ),
        ),
        ExpansionPanel(
          isExpanded: _expandState[3],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(title: Text("文件访问"));
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                const Text("通过这里获取更多文件权限，以实现对特殊目录的访问。"),
                SizedBox.fromSize(size: const Size.square(16)),
                Wrap(
                    alignment: WrapAlignment.center,
                    spacing: 4.0,
                    runSpacing: 4.0,
                    children: [
                      OutlinedButton(
                          style: Default.commandButtonStyle,
                          child: const Text("申请存储权限"),
                          onPressed: () {
                            Permission.storage.request();
                          }),
                      OutlinedButton(
                          style: Default.commandButtonStyle,
                          child: const Text("申请所有文件访问权限"),
                          onPressed: () {
                            Permission.manageExternalStorage.request();
                          }),
                    ]),
                SizedBox.fromSize(size: const Size.square(16)),
              ],
            ),
          ),
        ),
        ExpansionPanel(
          isExpanded: _expandState[4],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(
                title: Text("virgl加速"), subtitle: Text("实验性功能"));
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                const Text(
                    """virgl加速可部分利用设备GPU提升系统图形处理表现，但由于设备差异也可能导致容器系统及软件运行不稳定甚至异常退出。

请先开启测试按钮启动virgl服务器，如果按钮没有自动关闭说明至少启动没问题；

不过运行情况依然无法保证。
"""),
                SizedBox.fromSize(size: const Size.square(16)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getGlobal("defaultVirglCommand") as String,
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(), labelText: "virgl服务器参数"),
                  onChanged: (value) async {
                    await Global.prefs.setString("defaultVirglCommand", value);
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("测试"),
                  subtitle: const Text("启动virgl_test_server"),
                  value: Global.isVirglServerStarted,
                  onChanged: (value) {
                    switch (value) {
                      case true:
                        {
                          Global.virglServerPty = Pty.start("/system/bin/sh");
                          Global.virglServerPty.write(const Utf8Encoder().convert(
                              "export CONTAINER_DIR=${Global.dataPath}/containers/${Global.currentContainer}\n${Global.dataPath}/bin/virgl_test_server ${Util.getGlobal("defaultVirglCommand")}\nexit\n"));
                          Global.virglServerPty.exitCode.then((value) {
                            Global.isVirglServerStarted = false;
                            setState(() {});
                          });
                        }
                        break;
                      case false:
                        {
                          Global.virglServerPty
                              .write(const Utf8Encoder().convert("\x03exit\n"));
                        }
                        break;
                    }
                    Global.isVirglServerStarted = value;
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(16)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getGlobal("defaultVirglOpt") as String,
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(), labelText: "图形环境变量"),
                  onChanged: (value) async {
                    await Global.prefs.setString("defaultVirglOpt", value);
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("启用virgl加速"),
                  subtitle: const Text("下次启动时生效"),
                  value: Util.getGlobal("virgl") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("virgl", value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(16)),
              ],
            ),
          ),
        ),
        ExpansionPanel(
            isExpanded: _expandState[5],
            headerBuilder: ((context, isExpanded) {
              return const ListTile(
                title: Text("跨架构/跨系统支持"),
                subtitle: Text("实验性功能"),
              );
            }),
            body: Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  children: [
                    const Text(
                        """使用box86/box64运行x86/x64架构的程序，或使用wine运行windows程序。

运行windows程序需要经过架构和系统两层模拟，不要对运行速度抱有期待。程序崩溃也是常有的。

你需要耐心。即使图形界面什么也没显示。看看终端，还在继续输出吗？还是停止在某个报错？

如果不耐烦可以去看个广告消磨时间（bushi）

或者寻找该windows软件官方是否提供linux arm64版本。

给高级用户的注意事项：
跨架构/跨系统提供类似binfmt_misc的支持。
你可以直接执行x86或x64的elf（系统会自动调用box86/box64），也可以直接执行exe文件（系统会自动调用wine64）。
前提是这些文件拥有可执行权限。"""),
                    SizedBox.fromSize(size: const Size.square(8)),
                    Wrap(
                        alignment: WrapAlignment.center,
                        spacing: 4.0,
                        runSpacing: 4.0,
                        children: [
                          OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: const Text("安装box86和box64"),
                              onPressed: () {
                                Util.termWrite(
                                    "bash ~/.local/share/tiny/extra/install-box");
                                Global.screenIndex.value = 0;
                              }),
                          OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: const Text("安装wine"),
                              onPressed: () async {
                                if (!await File(
                                        "${Global.dataPath}/tiny/cross/box64")
                                    .exists()) {
                                  if (!context.mounted) return;
                                  ScaffoldMessenger.of(context)
                                      .hideCurrentSnackBar();
                                  ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                          content: Text("请先安装box86/box64")));
                                  return;
                                }
                                Util.termWrite(
                                    "bash ~/.local/share/tiny/extra/install-wine");
                                Global.screenIndex.value = 0;
                              }),
                          OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: const Text("安装dxvk"),
                              onPressed: () async {
                                if (!Global.wasWineEnabled) {
                                  if (!context.mounted) return;
                                  ScaffoldMessenger.of(context)
                                      .hideCurrentSnackBar();
                                  ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                          content: Text("请启用wine后重试")));
                                  return;
                                }
                                Util.termWrite(
                                    "bash ~/.local/share/tiny/extra/install-dxvk");
                                Global.screenIndex.value = 0;
                              }),
                          OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: const Text("移除所有安装"),
                              onPressed: () async {
                                if (Global.wasBoxEnabled) {
                                  if (!context.mounted) return;
                                  ScaffoldMessenger.of(context)
                                      .hideCurrentSnackBar();
                                  ScaffoldMessenger.of(context).showSnackBar(
                                      const SnackBar(
                                          content: Text("请关闭跨架构支持后重试")));
                                  return;
                                }
                                Util.termWrite(
                                    "rm -rf ~/.local/share/tiny/cross");
                                Global.screenIndex.value = 0;
                              }),
                          OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: const Text("清空wine数据"),
                              onPressed: () async {
                                Util.termWrite("rm -rf ~/.wine");
                                Global.screenIndex.value = 0;
                              }),
                        ]),
                    SizedBox.fromSize(size: const Size.square(16)),
                    const Divider(height: 2, indent: 8, endIndent: 8),
                    SizedBox.fromSize(size: const Size.square(16)),
                    const Text("""开启wine后的常用指令，点击后前往图形界面耐心等待。

任意程序启动参考时间：
虎贲T7510 6GB 超过一分钟
骁龙870 12GB 约10秒
骁龙8gen3 不支持32位 可能不可用

初始化时间：
可能比本软件初始化还长
"""),
                    SizedBox.fromSize(size: const Size.square(8)),
                    Wrap(
                        alignment: WrapAlignment.center,
                        spacing: 4.0,
                        runSpacing: 4.0,
                        children: Default.wineCommands
                            .asMap()
                            .entries
                            .map<Widget>((e) {
                          return OutlinedButton(
                              style: Default.commandButtonStyle,
                              child: Text(e.value["name"]!),
                              onPressed: () {
                                Util.termWrite("${e.value["command"]!} &");
                                Global.screenIndex.value = 0;
                              });
                        }).toList()),
                    SizedBox.fromSize(size: const Size.square(16)),
                    const Divider(height: 2, indent: 8, endIndent: 8),
                    SizedBox.fromSize(size: const Size.square(16)),
                    const Text("以下选项修改后将在下次启动软件时生效。"),
                    SizedBox.fromSize(size: const Size.square(8)),
                    SwitchListTile(
                      title: const Text("启用box86/box64"),
                      subtitle: const Text("运行跨架构软件"),
                      value: Util.getGlobal("isBoxEnabled") as bool,
                      onChanged: (value) async {
                        //检测box64是否存在，存在才开启
                        if (value &&
                            !await File("${Global.dataPath}/tiny/cross/box64")
                                .exists()) {
                          if (!context.mounted) return;
                          ScaffoldMessenger.of(context).hideCurrentSnackBar();
                          ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(content: Text("请先安装box86/box64")));
                          return;
                        }
                        Global.prefs.setBool("isBoxEnabled", value);
                        if (!value && Util.getGlobal("isWineEnabled")) {
                          Global.prefs.setBool("isWineEnabled", false);
                        }
                        setState(() {});
                      },
                    ),
                    SwitchListTile(
                      title: const Text("启用wine"),
                      subtitle: const Text("运行windows exe软件"),
                      value: Util.getGlobal("isWineEnabled") as bool,
                      onChanged: (value) async {
                        //检测wine是否存在且box64是否开启
                        if (value &&
                            !(Util.getGlobal("isBoxEnabled") &&
                                await File(
                                        "${Global.dataPath}/tiny/cross/wine/bin/wine")
                                    .exists())) {
                          if (!context.mounted) return;
                          ScaffoldMessenger.of(context).hideCurrentSnackBar();
                          ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                  content: Text("请先安装wine并启用box86/box64")));
                          return;
                        }
                        Util.execute(value
                            ? """filename="${Global.dataPath}/containers/${Global.currentContainer}/home/tiny/.bashrc"
command="export PATH=\\\$HOME/.local/share/tiny/cross/wine/bin:\\\$PATH # Auto-generated, do NOT edit"
if ! ${Global.dataPath}/busybox grep -qF "\$command" "\$filename"; then
    echo "\$command" >> "\$filename"
fi"""
                            : """filename="${Global.dataPath}/containers/${Global.currentContainer}/home/tiny/.bashrc"
command="export PATH=\\\$HOME/.local/share/tiny/cross/wine/bin:\\\$PATH # Auto-generated, do NOT edit"
if ${Global.dataPath}/busybox grep -qF "\$command" "\$filename"; then
    command="export PATH=\\\$HOME/.local/share/tiny/cross/wine/bin:\\\$PATH \\\\# Auto-generated, do NOT edit"
    ${Global.dataPath}/busybox sed -i "\\\\#\$command#Default" "\$filename"
fi""");
                        Global.prefs.setBool("isWineEnabled", value);
                        setState(() {});
                      },
                    ),
                    SizedBox.fromSize(size: const Size.square(16)),
                  ],
                ))),
        ExpansionPanel(
          isExpanded: _expandState[6],
          headerBuilder: ((context, isExpanded) {
            return const ListTile(
                title: Text("高分辨率支持"), subtitle: Text("实验性功能"));
          }),
          body: Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                const Text("""为更大的屏幕带来更高清的体验！

注意：
选项开启后显示会变得很大，请在图形界面的左栏设置里手动调整缩放到一个你认为合适的值。

一些软件可能会存在显示问题，或者显示速度变慢。"""),
                SizedBox.fromSize(size: const Size.square(16)),
                TextFormField(
                  maxLines: null,
                  initialValue: Util.getGlobal("defaultHidpiOpt") as String,
                  decoration: const InputDecoration(
                      border: OutlineInputBorder(), labelText: "HiDPI环境变量"),
                  onChanged: (value) async {
                    await Global.prefs.setString("defaultHidpiOpt", value);
                  },
                ),
                SizedBox.fromSize(size: const Size.square(8)),
                SwitchListTile(
                  title: const Text("高分辨率支持"),
                  subtitle: const Text("下次启动时生效"),
                  value: Util.getGlobal("isHidpiEnabled") as bool,
                  onChanged: (value) {
                    Global.prefs.setBool("isHidpiEnabled", value);
                    setState(() {});
                  },
                ),
                SizedBox.fromSize(size: const Size.square(16)),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
