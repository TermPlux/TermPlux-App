import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/material.dart';

class WindowTitleBar extends StatelessWidget {
  const WindowTitleBar({super.key, required this.enable});

  final bool enable;

  Widget? titleBar() {
    if (enable) {
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
