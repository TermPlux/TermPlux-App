import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';

import 'layout/home.dart';

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
