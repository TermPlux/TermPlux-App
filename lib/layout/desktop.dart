import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:termplux_app/value/global.dart';
import 'package:webview_flutter/webview_flutter.dart';

class DesktopPage extends StatelessWidget {
  const DesktopPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Focus(
      onKey: (node, event) {
        if (!kIsWeb) {
          if ({
            LogicalKeyboardKey.arrowLeft,
            LogicalKeyboardKey.arrowRight,
            LogicalKeyboardKey.arrowUp,
            LogicalKeyboardKey.arrowDown,
            LogicalKeyboardKey.tab
          }.contains(event.logicalKey)) {
            return KeyEventResult.skipRemainingHandlers;
          }
        }
        return KeyEventResult.ignored;
      },
      child: GestureDetector(
        onSecondaryTap: () {},
        child: WebViewWidget(
          controller: Global.controller,
        ),
      ),
    );
  }
}
