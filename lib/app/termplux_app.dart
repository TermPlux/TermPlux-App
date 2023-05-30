import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:termplux/app/boost_material_app.dart';
import 'package:termplux/app/preview_material_app.dart';

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

  Widget appBuilder(BuildContext context, Widget? home) {
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      if (kIsUseBoost) {
        return BoostMaterialApp(
            home: home!,
            appName: appName,
            lightColorScheme: lightColorScheme,
            darkColorScheme: darkColorScheme);
      } else {
        return PreviewMaterialApp(
            routes: routes,
            appName: appName,
            lightColorScheme: lightColorScheme,
            darkColorScheme: darkColorScheme);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    if (kIsUseBoost) {
      return FlutterBoostApp(routeFactory, appBuilder: (home) {
        return appBuilder(context, home);
      });
    } else {
      return DevicePreview(
        builder: (context) {
          return appBuilder(context, null);
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
