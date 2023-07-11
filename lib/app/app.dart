import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import '../pages/android.dart';
import '../pages/ios.dart';
import '../pages/windows.dart';
import '../pages/macos.dart';
import '../pages/linux.dart';
import '../platform/platform.dart';
import '../pages/home.dart';

class TermPluxApp extends StatefulWidget {
  const TermPluxApp({super.key});

  @override
  State<TermPluxApp> createState() => _TermPluxAppState();
}

class _TermPluxAppState extends State<TermPluxApp> {
  static const String appName = "TermPlux";

  @override
  void initState() {
    super.initState();
  }

  // ”/“为主页，其他为子页面
  static Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (settings, uniqueId) {
      return MaterialPageRoute<dynamic>(
          settings: settings,
          builder: (_) {
            return const MyHomePage();
          });
      // return PageRouteBuilder<dynamic>(
      //     settings: settings,
      //     pageBuilder: (_, __, ___) {
      //       return const MyHomePage();
      //     });
    },
    '/android': (settings, uniqueId) {
      return MaterialPageRoute<dynamic>(
          settings: settings,
          builder: (_) {
            return const AndroidPlatformPage();
          });
      // return PageRouteBuilder<dynamic>(
      //     settings: settings,
      //     pageBuilder: (_, __, ___) {
      //       return const AndroidPlatformPage();
      //     });
    },
    '/ios': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const IOSPlatformPage();
          });
      // return CupertinoPageRoute(
      //     settings: settings,
      //     builder: (_) {
      //       return const IOSPlatformPage();
      //     });
    },
    '/windows': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const WindowsPlatformPage();
          });
    },
    '/macos': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const MacOSPlatformPage();
          });
    },
    '/linux': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) {
            return const LinuxPlatformPage();
          });
    },
  };

  // ”/“为主页，其他为子页面
  static Map<String, Widget Function(BuildContext)> routes = {
    '/android': (context) => const AndroidPlatformPage(),
    '/ios': (context) => const IOSPlatformPage(),
    '/windows': (context) => const WindowsPlatformPage(),
    '/macos': (context) => const MacOSPlatformPage(),
    '/linux': (context) => const LinuxPlatformPage()
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory? func = routerMap[settings.name!];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  Widget appBuilder(BuildContext context, Widget home, bool dynamicColors) {
    if (dynamicColors) {
      return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
        return MaterialApp(
            routes: routes,
            home: home,
            builder: (context, child) {
              if (kIsUseBoost) {
                return home;
              } else {
                return DevicePreview.appBuilder(context, child);
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
            // ignore: deprecated_member_use
            useInheritedMediaQuery: true);
      });
    } else {
      return MaterialApp(
          routes: routes,
          home: home,
          builder: (context, child) {
            if (kIsUseBoost) {
              return home;
            } else {
              return DevicePreview.appBuilder(context, child);
            }
          },
          title: appName,
          theme: ThemeData(
              primaryColor: Colors.purple,
              brightness: Brightness.light,
              useMaterial3: true),
          darkTheme: ThemeData(
              primaryColor: Colors.purple,
              brightness: Brightness.dark,
              useMaterial3: true),
          themeMode: ThemeMode.system,
          locale: DevicePreview.locale(context),
          debugShowCheckedModeBanner: false,
          // ignore: deprecated_member_use
          useInheritedMediaQuery: true);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (kIsUseBoost) {
      return DevicePreview(
        builder: (context) {
          return FlutterBoostApp(routeFactory, appBuilder: (home) {
            return appBuilder(context, home, true);
          });
        },
        isToolbarVisible: true,
        availableLocales: const [
          Locale('zh_CN'),
        ],
        enabled: true,
      );
    } else {
      return DevicePreview(
        builder: (context) {
          return appBuilder(context, const MyHomePage(), false);
        },
        isToolbarVisible: true,
        availableLocales: const [
          Locale('zh_CN'),
        ],
        enabled: true,
      );
    }
  }
}
