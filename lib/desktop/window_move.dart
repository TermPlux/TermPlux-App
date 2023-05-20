import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/cupertino.dart';

class WindowMove extends StatelessWidget {
  const WindowMove({super.key, required this.enable, required this.tip});

  final bool enable;
  final String tip;

  Widget? move() {
    if (enable) {
      return Stack(
          alignment: Alignment.center,
          children: [Text(tip), WindowTitleBarBox(child: MoveWindow())]);
    } else {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(child: move() ?? Container());
  }
}
