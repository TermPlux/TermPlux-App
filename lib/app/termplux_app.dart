import 'package:device_preview/device_preview.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:termplux/app/app_builder.dart';

import '../platform/platform.dart';
import '../pages/home.dart';

class TermPluxApp extends StatelessWidget {
  const TermPluxApp({super.key});

  static const String appName = "TermPlux";

  // ”/“为主页，其他为子页面
  static Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const MyHomePage(title: appName);
          });
    },
  };

  // ”/“为主页，其他为子页面
  static Map<String, Widget Function(BuildContext)> routes = {
    '/': (context) => const MyHomePage(title: appName),
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory? func = routerMap[settings.name!];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  @override
  Widget build(BuildContext context) {
    if (kIsUseBoost) {
      return FlutterBoostApp(routeFactory, appBuilder: (home) {
        return AppBuilder(home: home, appName: appName, routes: null);
      });
    } else {
      return DevicePreview(
        builder: (context) {
          return AppBuilder(home: null, appName: appName, routes: routes);
        },
        isToolbarVisible: true,
        availableLocales: const [
          Locale('zh_CN'),
        ],
        enabled: kIsUsePreview,
      );
    }
  }
}
