import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:termplux/platform/platform.dart';
import 'package:url_strategy/url_strategy.dart';

import 'app/app.dart';
import 'boost/binding.dart';
import 'boost/observer.dart';

/// 应用名称
const String appName = 'TermPlux';

/// 默认窗口大小
const Size initialSize = Size(1280, 720);

/// 最小窗口大小
const Size minSize = Size(600, 450);

/// 应用入口函数
void main() async {
  if (kIsUseBoost) {
    PageVisibilityBinding.instance
        .addGlobalObserver(AppGlobalPageVisibilityObserver());
    CustomFlutterBinding();
  }
  if (kIsWeb) {
    // 在Web中隐藏井号
    setPathUrlStrategy();
  }
  // 启动应用
  runApp(const TermPluxApp());
  if (kIsDesktop) {
    doWhenWindowReady(() {
      appWindow.minSize = minSize;
      appWindow.size = initialSize;
      appWindow.alignment = Alignment.center;
      appWindow.title = appName;
      appWindow.show();
    });
  }
}
