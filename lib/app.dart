import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';

import 'home.dart';

class TermPluxApp extends StatefulWidget {
  const TermPluxApp({super.key});



  // @override
  // Widget build(BuildContext context) {
  //   return
  // }

  @override
  State<StatefulWidget> createState() => _TermPluxApp();
}

class _TermPluxApp extends State<TermPluxApp> {

  static const String appName = "TermPlux";



  @override
  Widget build(BuildContext context) {
   return DevicePreview(
       builder: (context) =>
           DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
             return MaterialApp(
               locale: DevicePreview.locale(context),
               builder: DevicePreview.appBuilder,
               title: appName,
               theme: ThemeData(
                   colorScheme: lightColorScheme,
                   brightness: Brightness.light,
                   useMaterial3: true),
               darkTheme: ThemeData(
                 colorScheme: darkColorScheme,
                 brightness: Brightness.dark,
                 useMaterial3: true,
               ),
               themeMode: ThemeMode.system,
               home: const MyHomePage(title: appName),
             );
           }),
       isToolbarVisible: true,
       availableLocales: const [
         Locale('zh_CN'),
       ],
       enabled: true);

  }

}