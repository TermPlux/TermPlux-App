import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/cupertino.dart';
import 'package:termplux/app/preview_material_app.dart';

import '../platform/platform.dart';
import 'boost_material_app.dart';

class AppBuilder extends StatelessWidget {
  const AppBuilder(
      {super.key, this.home, required this.appName, this.routes});

  final Widget? home;
  final String appName;
  final Map<String, WidgetBuilder>? routes;

  @override
  Widget build(BuildContext context) {
    return DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
      if (kIsUseBoost) {
        return BoostMaterialApp(
            home: home!,
            appName: appName,
            lightColorScheme: lightColorScheme,
            darkColorScheme: darkColorScheme);
      } else {
        return PreviewMaterialApp(
            routes: routes!,
            appName: appName,
            lightColorScheme: lightColorScheme,
            darkColorScheme: darkColorScheme);
      }
    });
  }
}
