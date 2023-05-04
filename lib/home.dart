import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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

  // void navToLauncher() {
  //   if (!kIsWeb) {
  //     if (Platform.isAndroid) {
  //       channel.invokeMethod("navToLauncher");
  //     }
  //   }
  // }

  AppBar? topAppBar() {
    if (!kIsWeb){
      if (!Platform.isAndroid){
        return AppBar(
          title: Text(widget.title),
        );
      } else {
        return null;
      }
    } else {
      return AppBar(
        title: Text(widget.title),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              '请选择目标平台:',
            ),
            MaterialButton(
              onPressed: _incrementCounter,
              child: const Text("Android")
            ),
            MaterialButton(
                onPressed: _incrementCounter,
                child: const Text("iOS")
            ),
            MaterialButton(
                onPressed: _incrementCounter,
                child: const Text("Windows")
            ),
            MaterialButton(
                onPressed: _incrementCounter,
                child: const Text("macOS")
            ),
            MaterialButton(
                onPressed: _incrementCounter,
                child: const Text("Linux")
            ),
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
