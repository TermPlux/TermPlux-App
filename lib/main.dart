import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'app/app.dart';
import 'boost/binding.dart';
import 'boost/observer.dart';

Future main() async {
  if (isUseBoost) {
    PageVisibilityBinding.instance
        .addGlobalObserver(AppGlobalPageVisibilityObserver());
    CustomFlutterBinding();
  }

  if (isDesktop) {
    WidgetsFlutterBinding.ensureInitialized();

    doWhenWindowReady(() {
      const initialSize = Size(1280, 720);
      appWindow.minSize = initialSize;
      appWindow.size = initialSize;
      appWindow.alignment = Alignment.center;
      appWindow.title = 'TermPlux';
      appWindow.show();
    });
  }

  runApp(const TermPluxApp());
}

bool get isDesktop {
  if (kIsWeb) return false;
  return [
    TargetPlatform.windows,
    TargetPlatform.linux,
    TargetPlatform.macOS,
  ].contains(defaultTargetPlatform);
}

bool get isUseBoost {
  if (kIsWeb) return false;
  return [
    TargetPlatform.android,
    TargetPlatform.iOS,
  ].contains(defaultTargetPlatform);
}
