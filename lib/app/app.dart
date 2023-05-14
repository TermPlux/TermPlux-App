import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:window_manager/window_manager.dart';

import '../pages/home.dart';

class TermPluxApp extends StatefulWidget {
  const TermPluxApp({super.key});

  @override
  State<StatefulWidget> createState() => _TermPluxApp();
}

class _TermPluxApp extends State<TermPluxApp> with WindowListener {
  static const String appName = "TermPlux";

  @override
  void initState() {
    if (isDesktop) windowManager.addListener(this);
    super.initState();
  }

  @override
  void dispose() {
    if (isDesktop) windowManager.removeListener(this);
    super.dispose();
  }

  @override
  void onWindowFocus() {
    setState;
    super.onWindowFocus();
  }

  static Map<String, FlutterBoostRouteFactory> routerMap = {
    'home': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => const MyHomePage(title: appName));
    },
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory? func = routerMap[settings.name!];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  Widget mbuilder(BuildContext context, Widget? widget, Widget home) {
    if (isUseBoost) return home;
    return DevicePreview.appBuilder(context, widget);
  }

  Widget appBuilder(Widget home) {
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      return MaterialApp(
        useInheritedMediaQuery: true,
        locale: DevicePreview.locale(context),
        builder: (context, widget) => mbuilder(context, widget, home),
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
        home: home,
        debugShowCheckedModeBanner: false,
      );
    });
  }

  bool get isUsePreview {
    if (kIsWeb) return true;
    return ![
      TargetPlatform.android,
      TargetPlatform.iOS,
    ].contains(defaultTargetPlatform);
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

  @override
  Widget build(BuildContext context) {
    if (isUseBoost) {
      return FlutterBoostApp(routeFactory, appBuilder: appBuilder);
    }
    return DevicePreview(
      builder: (context) {
        return appBuilder(const MyHomePage(title: appName));
      },
      isToolbarVisible: true,
      availableLocales: const [
        Locale('zh_CN'),
      ],
      enabled: isUsePreview,
    );
  }
}
