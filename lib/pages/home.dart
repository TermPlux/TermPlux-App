import 'dart:io';

import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:termplux/desktop/window_buttons.dart';

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

  void aaa() {
    BoostNavigator.instance.push("home", withContainer: false, opaque: true);
  }

  bool get isUseBoost {
    if (kIsWeb) return false;
    return [
      TargetPlatform.android,
      TargetPlatform.iOS,
    ].contains(defaultTargetPlatform);
  }

  bool get isDesktop {
    if (kIsWeb) return false;
    return [
      TargetPlatform.windows,
      TargetPlatform.linux,
      TargetPlatform.macOS,
    ].contains(defaultTargetPlatform);
  }

  AppBar topAppBar() {
    if (isDesktop) {
      return AppBar(
        title: WindowTitleBarBox(child: MoveWindow()),
        actions: const [WindowButtons()],
      );
    } else {
      return AppBar(title: const Text('TermPlux'));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: topAppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              '你这个BYD的点击数:',
            ),
            TextButton(onPressed: navToPager, child: const Text("PAGE")),
            TextButton(onPressed: aaa, child: const Text("PUSH")),
            // const Text(
            //   '请选择目标平台:',
            // ),
            // MaterialButton(
            //   onPressed: _incrementCounter,
            //   child: const Text("Android")
            // ),
            // MaterialButton(
            //     onPressed: _incrementCounter,
            //     child: const Text("iOS")
            // ),
            // MaterialButton(
            //     onPressed: _incrementCounter,
            //     child: const Text("Windows")
            // ),
            // MaterialButton(
            //     onPressed: _incrementCounter,
            //     child: const Text("macOS")
            // ),
            // MaterialButton(
            //     onPressed: _incrementCounter,
            //     child: const Text("Linux")
            // ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '增加',
        child: const Icon(Icons.add),
      ),
    );
  }
}
