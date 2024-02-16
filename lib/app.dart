import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/material.dart';
import 'package:termplux_app/value/default.dart';

import 'layout/home.dart';

class MyApp extends StatelessWidget {
  const MyApp({super.key});



  @override
  Widget build(BuildContext context) {
    return DynamicColorBuilder(
      builder: (lightDynamic, darkDynamic) {
        return MaterialApp(
          title: Default.appName,
          theme: ThemeData(
            colorScheme: lightDynamic,
            useMaterial3: true,
          ),
          darkTheme: ThemeData(
            colorScheme: darkDynamic,
            useMaterial3: true,
          ),
          home: const Home(title: Default.appName),
        );
      },
    );
  }
}
