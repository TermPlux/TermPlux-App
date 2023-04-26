import 'package:device_preview/device_preview.dart';
import 'package:dynamic_color/dynamic_color.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'home.dart';

class TermPluxApp extends StatelessWidget {
  const TermPluxApp({super.key});

  static const String appName = "TermPlux";

  // /// 由于很多同学说没有跳转动画，这里是因为之前exmaple里面用的是 [PageRouteBuilder]，
  // /// 其实这里是可以自定义的，和Boost没太多关系，比如我想用类似iOS平台的动画，
  // /// 那么只需要像下面这样写成 [CupertinoPageRoute] 即可
  // /// (这里全写成[MaterialPageRoute]也行，这里只不过用[CupertinoPageRoute]举例子)
  // ///
  // /// 注意，如果需要push的时候，两个页面都需要动的话，
  // /// （就是像iOS native那样，在push的时候，前面一个页面也会向左推一段距离）
  // /// 那么前后两个页面都必须是遵循CupertinoRouteTransitionMixin的路由
  // /// 简单来说，就两个页面都是CupertinoPageRoute就好
  // /// 如果用MaterialPageRoute的话同理
  //
  // Map<String, FlutterBoostRouteFactory> routerMap = {
  //   'mainPage': (settings, uniqueId) {
  //     return CupertinoPageRoute(
  //         settings: settings,
  //         builder: (_) {
  //           Map<String, Object> map = settings.arguments as Map<String, Object>;
  //           String data = map['data'] as String;
  //           return SimplePage(
  //             data: data,
  //           );
  //         });
  //   },
  // };
  //
  // Route<dynamic> routeFactory(RouteSettings settings, String uniqueId) {
  //   FlutterBoostRouteFactory func = routerMap[settings.name] as FlutterBoostRouteFactory;
  //   return func(settings, uniqueId);
  // }

  Widget appBuilder(Widget home) {
    return DevicePreview(
        builder: (context) =>
            DynamicColorBuilder(builder: (lightColorScheme, darkColorScheme) {
              return MaterialApp(
                locale: DevicePreview.locale(context),
             //   builder: DevicePreview.appBuilder,
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
                debugShowCheckedModeBanner: true,
                builder: (_, __) {
                  DevicePreview.appBuilder;
                  return home;
                },
              );
            }),
        isToolbarVisible: true,
        availableLocales: const [Locale('zh_CN')],
        enabled: true);
    // return MaterialApp(
    //   home: home,
    //   debugShowCheckedModeBanner: true,
    //
    //   ///必须加上builder参数，否则showDialog等会出问题
    //   builder: (_, __) {
    //     return home;
    //   },
    // );
  }

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
                    useMaterial3: true),
                themeMode: ThemeMode.system,
                home: const MyHomePage(title: appName),
              );
            }),
        isToolbarVisible: true,
        availableLocales: const [Locale('zh_CN')],
        enabled: true);
  }
}
