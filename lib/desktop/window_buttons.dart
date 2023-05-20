import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/material.dart';

final buttonColors = WindowButtonColors(
    mouseOver: const Color(0x33000000),
    mouseDown: const Color(0x1A000000),
    iconNormal: const Color(0xE6000000),
    iconMouseOver: const Color(0xE6000000),
    iconMouseDown: Colors.black);

final closeButtonColors = WindowButtonColors(
    mouseOver: const Color(0xFFD32F2F),
    mouseDown: const Color(0xFFB71C1C),
    iconNormal: const Color(0xE6000000),
    iconMouseOver: Colors.white);

class WindowButtons extends StatelessWidget {
  const WindowButtons({super.key, required this.enable});

  final bool enable;

  Widget? button() {
    if (enable) {
      return Row(children: <Widget>[
        RestoreWindowButton(),
        MinimizeWindowButton(colors: buttonColors),
        MaximizeWindowButton(colors: buttonColors),
        CloseWindowButton(colors: closeButtonColors)
      ]);
    } else {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(child: button() ?? Container());
  }
}
