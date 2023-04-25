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

  void navToLauncher() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        channel.invokeMethod("navToLauncher");
      }
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
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            TextButton(onPressed: _incrementCounter, child: const Text("增加"))
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: navToLauncher,
        tooltip: 'Launcher',
        child: const Icon(Icons.arrow_forward),
      ),
    );
  }
}
