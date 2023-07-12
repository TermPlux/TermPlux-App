import 'package:bitsdojo_window/bitsdojo_window.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:url_strategy/url_strategy.dart';

import 'app/app.dart';
import 'platform/platform.dart';
import 'boost/binding.dart';
import 'boost/observer.dart';

///                          _ooOoo_
///                         o8888888o
///                         88" . "88
///                         (| ^_^ |)
///                         O\  =  /O
///                      ____/`---'\____
///                    .'  \\|     |//  `.
///                   /  \\|||  :  |||//  \
///                  /  _||||| -:- |||||-  \
///                  |   | \\\  -  /// |   |
///                  | \_|  ''\---/''  |   |
///                  \  .-\__  `-`  ___/-. /
///                ___`. .'  /--.--\  `. . ___
///              ."" '<  `.___\_<|>_/___.'  >'"".
///            | | :  `- \`.;`\ _ /`;.`/ - ` : | |
///            \  \ `-.   \_ __\ /__ _/   .-` /  /
///      ========`-.____`-.___\_____/___.-`____.-'========
///                           `=---='
///      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
///              佛祖保佑       永不宕机     永无BUG

/// 应用名称
const String appName = 'TermPlux';

/// 默认窗口大小
const Size initialSize = Size(1280, 720);

/// 最小窗口大小
const Size minSize = Size(600, 450);

/// 应用入口函数
Future main() async {
  // FlutterBoost
  if (kIsUseBoost) {
    PageVisibilityBinding.instance
        .addGlobalObserver(AppGlobalPageVisibilityObserver());
    CustomFlutterBinding();
  }

  // 在Web中隐藏井号
  if (kIsWeb) setPathUrlStrategy();

  WidgetsFlutterBinding.ensureInitialized();

  // 窗口管理
  if (kIsDesktop) {
    doWhenWindowReady(() {
      appWindow.minSize = minSize;
      appWindow.size = initialSize;
      appWindow.alignment = Alignment.center;
      appWindow.title = appName;
      appWindow.show();
    });
  }

  // 启用边到边
  if (kIsMobile) SystemChrome.setEnabledSystemUIMode(SystemUiMode.edgeToEdge);

  // 启动应用
  runApp(const TermPluxApp());
}
