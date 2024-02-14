import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:termplux_app/layout/contor.dart';

import '../main.dart';
import '../value/global.dart';
import '../utils/util.dart';
import '../workflow/workflow.dart';
import 'desktop.dart';
import 'loading.dart';
import 'terminal.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  //安装完成了吗？
  //完成后从加载界面切换到主界面
  bool isLoadingComplete = false;

  @override
  void initState() {
    if (!isLoadingComplete) {
      Workflow.workflow().then((value) {
        setState(() {
          isLoadingComplete = true;
        });
      });
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          isLoadingComplete
              ? Util.getCurrentProp("name")
              : widget.title,
        ),
      ),
      body: isLoadingComplete ? Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Expanded(
            child: ValueListenableBuilder(
              valueListenable: Global.pageIndex,
              builder: (context, value, child) {
                return IndexedStack(
                  index: Global.pageIndex.value,
                  children: const [
                    TerminalPage(),
                    DesktopPage(),
                    Contor()
                  ],
                );
              },
            ),
          ),
        ],
      ) : LoadingPage(),
      bottomNavigationBar: ValueListenableBuilder(
        valueListenable: Global.pageIndex,
        builder: (context, value, child) {
          return Visibility(
            visible: isLoadingComplete,
            child: NavigationBar(
              selectedIndex: Global.pageIndex.value,
              destinations: const [
                NavigationDestination(
                  icon: Icon(Icons.terminal_outlined),
                  selectedIcon: Icon(Icons.terminal),
                  label: "终端",
                  tooltip: "终端模拟器",
                  enabled: true,
                ),
                NavigationDestination(
                  icon: Icon(Icons.desktop_windows_outlined),
                  selectedIcon: Icon(Icons.desktop_windows),
                  label: "桌面",
                  tooltip: '桌面',
                  enabled: true,
                ),
                NavigationDestination(
                  icon: Icon(Icons.video_settings_outlined),
                  selectedIcon: Icon(Icons.video_settings),
                  label: "控制",
                  tooltip: '控制',
                  enabled: true,
                )
              ],
              onDestinationSelected: (index) {
                Global.pageIndex.value = index;
              },
            ),
          );
        },
      ),
    );
  }
}
