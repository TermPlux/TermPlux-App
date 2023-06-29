import 'package:flutter/material.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';

class MacOSPlatformPage extends StatefulWidget {
  const MacOSPlatformPage({super.key});

  @override
  State<MacOSPlatformPage> createState() => _MacOSPlatformPageState();
}

class _MacOSPlatformPageState extends State<MacOSPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("macOS"),
        flexibleSpace: const WindowTitleBar(),
        actions: [const WindowButtons()],
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
