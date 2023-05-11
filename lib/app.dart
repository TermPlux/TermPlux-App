import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';

import 'home.dart';

class TermPluxApp extends StatefulWidget {
  const TermPluxApp({super.key});

  @override
  State<StatefulWidget> createState() => _TermPluxApp();
}

class _TermPluxApp extends State<TermPluxApp> {
  static const String appName = "TermPlux";

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

  @override
  void initState() {
    super.initState();
  }

  Widget appBuilder(Widget home) {
    return DevicePreview(
      builder: (context) {
        return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
          return MaterialApp(
            useInheritedMediaQuery: true,
            locale: DevicePreview.locale(context),
            builder: EasyLoading.init(builder: (context, child) {
              child = DevicePreview.appBuilder(context, child);
              child = home;
              return child;
            }),
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
      }, // Wrap your app
      isToolbarVisible: true,
      availableLocales: const [Locale('zh_CN')],
      enabled: true,
    );
  }

  @override
  Widget build(BuildContext context) {
    return FlutterBoostApp(
      routeFactory,
      appBuilder: appBuilder,
    );
  }
}
