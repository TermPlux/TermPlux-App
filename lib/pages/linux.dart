import 'package:flutter/material.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';

class LinuxPlatformPage extends StatefulWidget {
  const LinuxPlatformPage({super.key});

  @override
  State<LinuxPlatformPage> createState() => _LinuxPlatformPageState();
}

class _LinuxPlatformPageState extends State<LinuxPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Linux"),
        flexibleSpace: const WindowTitleBar(),
        actions: [const WindowButtons()],
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
