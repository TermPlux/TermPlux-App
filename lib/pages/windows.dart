import 'package:flutter/material.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';

class WindowsPlatformPage extends StatefulWidget {
  const WindowsPlatformPage({super.key});

  @override
  State<WindowsPlatformPage> createState() => _WindowsPlatformPageState();
}

class _WindowsPlatformPageState extends State<WindowsPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Windows"),
        flexibleSpace: const WindowTitleBar(),
        actions: [const WindowButtons()],
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
