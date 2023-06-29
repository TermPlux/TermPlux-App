import 'package:flutter/material.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';

class AndroidPlatformPage extends StatefulWidget {
  const AndroidPlatformPage({super.key});

  @override
  State<AndroidPlatformPage> createState() => _AndroidPlatformPageState();
}

class _AndroidPlatformPageState extends State<AndroidPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Android"),
        flexibleSpace: const WindowTitleBar(),
        actions: [const WindowButtons()],
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
