import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'layout/home.dart';
import 'layout/signal9error.dart';
import 'workflow/workflow.dart';

// class MyApp extends StatefulWidget {
//   const MyApp({super.key});
//
//   @override
//   State<MyApp> createState() => _MyAppState();
// }
//
// class _MyAppState extends State<MyApp> {
//   static const String appName = "TermPlux";
//
//   static Map<String, FlutterBoostRouteFactory> routerMap = {
//     '/': (settings, uniqueId) {
//       return MaterialPageRoute(
//           settings: settings,
//           builder: (_) {
//             return const MyHomePage(title: appName);
//           });
//     },
//     '/desktop': (settings, uniqueId) {
//       return MaterialPageRoute(
//         settings: settings,
//         builder: (_) => const Desktop(),
//       );
//     },
//     '/signal9': (settings, uniqueId) {
//       return MaterialPageRoute(
//         settings: settings,
//         builder: (_) => const Signal9Error(),
//       );
//     },
//   };
//
//   Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
//     FlutterBoostRouteFactory? func = routerMap[settings.name!];
//     if (func == null) return null;
//     return func(settings, uniqueId);
//   }
//
//   Widget appBuilder(BuildContext context, Widget home) {
//     return DynamicColorBuilder(
//       builder: (ColorScheme? lightDynamic, ColorScheme? darkDynamic) {
//         return MaterialApp(
//           home: home,
//           builder: (context, child) => home,
//           title: appName,
//           theme: ThemeData(
//             colorScheme: lightDynamic,
//             useMaterial3: true,
//           ),
//           darkTheme: ThemeData(
//             colorScheme: darkDynamic,
//             useMaterial3: true,
//           ),
//           themeMode: ThemeMode.system,
//         );
//       },
//     );
//   }
//
//   @override
//   void initState() {
//     Workflow.workflow().then((value) {});
//     super.initState();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return appBuilder(context, const MyHomePage(title: appName));
//     return FlutterBoostApp(
//       routeFactory,
//       appBuilder: (home) => appBuilder(context, home),
//     );
//   }
// }

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const String appName = 'TermPlux';

  @override
  Widget build(BuildContext context) {
    return DynamicColorBuilder(
      builder: (ColorScheme? lightDynamic, ColorScheme? darkDynamic) {
        return MaterialApp(
          title: appName,
          theme: ThemeData(
            colorScheme: lightDynamic,
            useMaterial3: true,
          ),
          darkTheme: ThemeData(
            colorScheme: darkDynamic,
            useMaterial3: true,
          ),
          home: const MyHomePage(title: appName),
        );
      },
    );
  }
}
