import 'package:flutter/material.dart';

import '../widget/command.dart';
import '../value/global.dart';
import 'desktop.dart';
import 'info.dart';
import 'loading.dart';
import 'settings.dart';
import 'terminal.dart';
import '../utils/util.dart';
import '../workflow/workflow.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  bool bannerAdsFailedToLoad = false;

  //安装完成了吗？
  //完成后从加载界面切换到主界面
  bool isLoadingComplete = false;

  @override
  void initState() {
    super.initState();
    if (!isLoadingComplete) {
      Workflow.workflow().then((value) {
        setState(() {
          isLoadingComplete = true;
        });
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          isLoadingComplete ? Util.getCurrentProp("name") : widget.title,
        ),
      ),
      body: isLoadingComplete
          ? Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Expanded(
                  child: ValueListenableBuilder(
                    valueListenable: Global.pageIndex,
                    builder: (context, value, child) {
                      return IndexedStack(
                        index: Global.pageIndex.value,
                        children: [
                          const TerminalPage(),
                          const DesktopPage(),
                          Padding(
                            padding: const EdgeInsets.all(8),
                            child: Scrollbar(
                              child: SingleChildScrollView(
                                restorationId: "control-scroll",
                                child: Column(
                                  children: [
                                    const Padding(
                                      padding: EdgeInsets.all(16),
                                      child: FractionallySizedBox(
                                        widthFactor: 0.4,
                                        child: FlutterLogo(),
                                      ),
                                    ),
                                    const FastCommands(),
                                    Padding(
                                      padding: const EdgeInsets.all(8),
                                      child: Card(
                                        child: Padding(
                                          padding: const EdgeInsets.all(8),
                                          child: Column(
                                            children: [
                                              const SettingPage(),
                                              SizedBox.fromSize(
                                                  size: const Size.square(8)),
                                              const InfoPage(
                                                openFirstInfo: false,
                                              )
                                            ],
                                          ),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                            ),
                          )
                        ],
                      );
                    },
                  ),
                ),
              ],
            )
          : const LoadingPage(),
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
