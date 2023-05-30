import 'package:device_preview/device_preview.dart';
import 'package:flutter/material.dart';

class PreviewMaterialApp extends StatelessWidget {
  const PreviewMaterialApp(
      {super.key,
      required this.routes,
      required this.appName,
      this.lightColorScheme,
      this.darkColorScheme});

  final Map<String, WidgetBuilder> routes;
  final String appName;
  final ColorScheme? lightColorScheme;
  final ColorScheme? darkColorScheme;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        routes: routes,
        builder: DevicePreview.appBuilder,
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
        locale: DevicePreview.locale(context));
  }
}
