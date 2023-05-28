import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import '../platform/platform.dart';
import '../pages/home.dart';

class TermPluxApp extends StatelessWidget {
  const TermPluxApp({super.key});

  static const String appName = "TermPlux";

  static Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const MyHomePage(title: appName);
          });
    },
  };

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

  String? initial() {
    if (!kIsUseBoost) return 'home';
    return null;
  }

  Widget appBuilder(BuildContext context, Widget home) {
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      return MaterialApp(
        home: home,
        routes: routes,
        initialRoute: initial(),
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
