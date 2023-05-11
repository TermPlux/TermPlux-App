import 'package:device_preview/device_preview.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'app.dart';
import 'boost/binding.dart';

Future main() async {
  PageVisibilityBinding.instance
      .addGlobalObserver(AppGlobalPageVisibilityObserver());
  CustomFlutterBinding();
  runApp(const TermPluxApp());

  // runApp(DevicePreview(
  //   enabled: !kReleaseMode,
  //   builder: (context) => const TermPluxApp(), // Wrap your app
  // ));
}

class AppGlobalPageVisibilityObserver extends GlobalPageVisibilityObserver {
  @override
  void onPagePush(Route<dynamic> route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onPageCreate route:${route.settings.name}');
  }

  @override
  void onPageShow(Route<dynamic> route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onPageShow route:${route.settings.name}');
  }

  @override
  void onPageHide(Route<dynamic> route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onPageHide route:${route.settings.name}');
  }

  @override
  void onPagePop(Route<dynamic> route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onPageDestroy route:${route.settings.name}');
  }

  @override
  void onForeground(Route route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onForeground route:${route.settings.name}');
  }

  @override
  void onBackground(Route<dynamic> route) {
    Logger.log(
        'boost_lifecycle: AppGlobalPageVisibilityObserver.onBackground route:${route.settings.name}');
  }
}
