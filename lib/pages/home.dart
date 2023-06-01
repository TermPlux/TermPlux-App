import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:termplux/widget/android_logo.dart';
import 'package:termplux/widget/apple_logo.dart';
import 'package:termplux/widget/chrome_logo.dart';

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
      body: LayoutBuilder(
        builder: (BuildContext context, BoxConstraints viewportConstraints) {
          return SingleChildScrollView(
            child: ConstrainedBox(
              constraints: BoxConstraints(
                  minHeight: viewportConstraints.maxHeight, minWidth: 50),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: <Widget>[
                  const Text(
                    '请选择目标平台:',
                    textAlign: TextAlign.center,
                  ),


                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("Android"),
                          AndroidSystemLogo()
                        ],
                      ),
                    ),
                  ),

                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("iOS"),
                          AppleSystemLogo()
                        ],
                      ),
                    ),
                  ),

                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("Windows"),
                          WindowsSystemLogo()
                        ],
                      ),
                    ),
                  ),

                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("macOS"),
                          AppleSystemLogo()
                        ],
                      ),
                    ),
                  ),

                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("Linux"),
                          LinuxSystemLogo()
                        ],
                      ),
                    ),
                  ),

                  GestureDetector(
                    onTap: aaa,
                    child: const Card(
                      child: Column(
                        children: [
                          Text("Web"),
                          ChromeBrowserLogo()
                        ],
                      ),
                    ),
                  ),





                  const FlutterLogo(),
                  TextButton(onPressed: aaa, child: const Text("PUSH")),
                  TextButton(onPressed: toggle, child: const Text("TOGGLE")),
                  Text(
                    '$_counter',
                    style: Theme.of(context).textTheme.headlineMedium,
                  ),
                ],
              ),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '增加',
        child: const Icon(Icons.add),
      ),
    );
  }
}
