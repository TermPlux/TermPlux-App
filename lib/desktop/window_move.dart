import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/material.dart';
import 'package:termplux/platform/platform.dart';

class WindowTitleBar extends StatelessWidget {
  const WindowTitleBar({super.key});

  Widget? titleBar() {
    if (kIsDesktop) {
      return MoveWindow();
    } else {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(child: titleBar() ?? Container());
  }
}
