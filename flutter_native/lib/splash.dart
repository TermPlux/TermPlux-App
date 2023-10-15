import 'package:flutter/material.dart';

// 闪屏页
class Splash extends StatelessWidget {
  const Splash({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: const Text('应用正在启动')),
        body: const Center(child: CircularProgressIndicator()));
  }
}
