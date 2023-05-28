import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

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

  // 子页面，不能用”/“，主页通过home传入
  static Map<String, Widget Function(BuildContext)> routes = {
    //'home': (context) => const MyHomePage(title: appName),
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory? func = routerMap[settings.name!];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  Widget appBuilder(BuildContext context, Widget home) {
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      return MaterialApp(
        home: home,
        routes: routes,
        builder: (context, widget) {
          if (kIsUseBoost) {
            return home;
          } else {
            return DevicePreview.appBuilder(context, widget);
          }
        },
        title: appName,
        theme: ThemeData(
            colorScheme: lightColorScheme,
            brightness: Brightness.light,
            useMaterial3: true),
        darkTheme: ThemeData(
            colorScheme: darkColorScheme,
            brightness: Brightness.dark,
            useMaterial3: true),
        themeMode: ThemeMode.system,
        locale: DevicePreview.locale(context),
        debugShowCheckedModeBanner: false,
      );
    });
  }

  /// 非boost走home和routes
  /// boost走专属路由
  @override
  Widget build(BuildContext context) {
    if (kIsUseBoost) {
      return FlutterBoostApp(routeFactory, appBuilder: (home) {
        return appBuilder(context, home);
      });
    } else {
      return DevicePreview(
        builder: (context) {
          return appBuilder(context, const MyHomePage(title: appName));
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
