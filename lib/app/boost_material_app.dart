import 'package:flutter/material.dart';

class BoostMaterialApp extends StatelessWidget {
  const BoostMaterialApp(
      {super.key,
      required this.home,
      required this.appName,
      required this.lightColorScheme,
      required this.darkColorScheme});

  final Widget home;
  final String appName;
  final ColorScheme? lightColorScheme;
  final ColorScheme? darkColorScheme;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: home,
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
        themeMode: ThemeMode.system);
  }
}
