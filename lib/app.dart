import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

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
          settings: settings, pageBuilder: (_, __, ___) => const MyHomePage(title: appName));
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
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      return MaterialApp(
        builder: (_, __) => home,
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

  @override
  Widget build(BuildContext context) {
    return FlutterBoostApp(
      routeFactory,
      appBuilder: appBuilder,
    );
    // return FlutterBoostApp(
    //   routeFactory,
    //     // 如果自定了appBuilder，需要将传入的参数添加到widget层次结构中去，
    //     // 否则会导致FluttBoost初始化失败。
    //     appBuilder: (child) => DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
    //       return MaterialApp(
    //         locale: DevicePreview.locale(context),
    //         builder: DevicePreview.appBuilder,
    //         title: appName,
    //         theme: ThemeData(
    //             colorScheme: lightColorScheme,
    //             brightness: Brightness.light,
    //             useMaterial3: true),
    //         darkTheme: ThemeData(
    //             colorScheme: darkColorScheme,
    //             brightness: Brightness.dark,
    //             useMaterial3: true),
    //         themeMode: ThemeMode.system,
    //         home: child,
    //         debugShowCheckedModeBanner: false,
    //       );
    //     }),
    //     // interceptors: [
    //     //   CustomInterceptor1(),
    //     //   CustomInterceptor2(),
    //     //   CustomInterceptor3(),
    //     // ]
    // );



    // return DevicePreview(
    //     builder: (context) =>
    //         DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
    //           return MaterialApp(
    //             locale: DevicePreview.locale(context),
    //             builder: DevicePreview.appBuilder,
    //             title: appName,
    //             theme: ThemeData(
    //                 colorScheme: lightColorScheme,
    //                 brightness: Brightness.light,
    //                 useMaterial3: true),
    //             darkTheme: ThemeData(
    //                 colorScheme: darkColorScheme,
    //                 brightness: Brightness.dark,
    //                 useMaterial3: true),
    //             themeMode: ThemeMode.system,
    //             home: const MyHomePage(title: appName),
    //             debugShowCheckedModeBanner: false,
    //           );
    //         }),
    //     isToolbarVisible: true,
    //     availableLocales: const [Locale('zh_CN')],
    //     enabled: kDebugMode);
  }
}