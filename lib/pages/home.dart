import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:termplux/widget/android_logo.dart';
import 'package:termplux/widget/apple_logo.dart';
import 'package:termplux/widget/chrome_logo.dart';
import 'package:termplux/widget/platform_card.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';
import '../platform/platform.dart';
import '../navigation/navigation.dart';
import '../widget/linux_logo.dart';
import '../widget/windows_logo.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  static const channel = MethodChannel('termplux_channel');

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  void navToPager() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        channel.invokeMethod("pager");
      }
    }
  }

  void toggle() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        channel.invokeMethod("toggle");
      }
    }
  }

  // void navigation(String route) {
  //   if (isUseBoost){
  //     BoostNavigator.instance.push(route, withContainer: false);
  //   } else {
  //     Navigator.pushNamed(context, route);
  //   }
  // }

  void aaa() {
    navigation(context, '/');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        flexibleSpace: WindowTitleBar(enable: kIsDesktop),
        actions: [
          IconButton(
              onPressed: navToPager, icon: const Icon(Icons.arrow_forward)),
          IconButton(onPressed: navToPager, icon: const Icon(Icons.more_vert)),
          WindowButtons(enable: kIsDesktop)
        ],
      ),
      body: Center(
        child: Scrollbar(
          child: ListView(
            padding: const EdgeInsets.only(top: 8, left: 8, right: 8),
            children: [
              const Text(
                '请选择目标平台:',
                textAlign: TextAlign.center,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "Android",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: AndroidSystemLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "iOS",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: AppleSystemLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "Windows",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: WindowsSystemLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "macOS",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: AppleSystemLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "Linux",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: LinuxSystemLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),






              GestureDetector(
                onTap: aaa,
                child: const Card(
                  child: Column(
                    children: [Text("Web"), ChromeBrowserLogo()],
                  ),
                ),
              ),
              const FlutterLogo(),
              TextButton(onPressed: aaa, child: const Text("PUSH")),
              TextButton(onPressed: toggle, child: const Text("TOGGLE")),
              Text(
                '$_counter',
                style: Theme
                    .of(context)
                    .textTheme
                    .headlineMedium,
              ),
            ],
          ),
        ),
      ),
      // body: Scrollbar(
      //   child: ListView(
      //     padding: const EdgeInsets.only(top: 8, left: 8, right: 8),
      //     children: [
      //
      //     ],
      //   ),
      // ),
      // body: LayoutBuilder(
      //   builder: (BuildContext context, BoxConstraints viewportConstraints) {
      //     return SingleChildScrollView(
      //       child: ConstrainedBox(
      //         constraints: BoxConstraints(
      //             minHeight: viewportConstraints.maxHeight, minWidth: 50),
      //         child: Column(
      //           mainAxisSize: MainAxisSize.min,
      //           crossAxisAlignment: CrossAxisAlignment.stretch,
      //           children: <Widget>[
      //
      //           ],
      //         ),
      //       ),
      //     );
      //   },
      // ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '增加',
        child: const Icon(Icons.add),
      ),
    );
  }
}
