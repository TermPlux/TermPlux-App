import 'package:flutter/material.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';

class IOSPlatformPage extends StatefulWidget {
  const IOSPlatformPage({super.key});

  @override
  State<IOSPlatformPage> createState() => _IOSPlatformPageState();
}

class _IOSPlatformPageState extends State<IOSPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("iOS"),
        flexibleSpace: const WindowTitleBar(),
        actions: [const WindowButtons()],
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
