import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'boost/binding.dart';
import 'boost/observer.dart';

Future main() async {
  PageVisibilityBinding.instance
      .addGlobalObserver(AppGlobalPageVisibilityObserver());
  CustomFlutterBinding();
  SystemChrome.setEnabledSystemUIMode(SystemUiMode.edgeToEdge);
  runApp(const TermPluxApp());
}

class TermPluxApp extends StatelessWidget {
  const TermPluxApp({super.key});

  static const String appName = "TermPlux-App";

  static Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (settings, uniqueId) {
      return CupertinoPageRoute(
          settings: settings,
          title: appName,
          builder: (_) {
            return const MyHomePage(title: appName);
          });
    },
  };

  Route<dynamic>? routeFactory(RouteSettings settings, String? uniqueId) {
    FlutterBoostRouteFactory? func = routerMap[settings.name!];
    if (func == null) {
      return null;
    }
    return func(settings, uniqueId);
  }

  Widget appBuilder(BuildContext context, Widget home) {
    return CupertinoApp(
      home: home,
      builder: (_, __) {
        return home;
      },
      title: appName,
      theme: const CupertinoThemeData(
        primaryColor: CupertinoColors.systemBlue,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return FlutterBoostApp(routeFactory, appBuilder: (home) {
      return appBuilder(context, home);
    });
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoTabScaffold(
      tabBar: CupertinoTabBar(
        currentIndex: 1,
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.phone), label: 'Calls'),
          BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.chat_bubble_2), label: 'Chats'),
          BottomNavigationBarItem(
              icon: Icon(CupertinoIcons.settings), label: 'Settings'),
        ],
      ),
      tabBuilder: (context, index) {
        late final CupertinoTabView returnValue;
        switch (index) {
          case 0:
            returnValue = CupertinoTabView(builder: (context) {
              return const CupertinoPageScaffold(
                  navigationBar: CupertinoNavigationBar(
                    middle: Text('Calls'),
                  ),
                  child: Center(child: Text('Calls')));
            });
            break;
          case 1:
            returnValue = CupertinoTabView(
              builder: (context) {
                return const CupertinoPageScaffold(
                    navigationBar: CupertinoNavigationBar(
                      middle: Text('Chats'),
                    ),
                    child: Center(child: Text('Chats')));
              },
            );
            break;
          case 2:
            returnValue = CupertinoTabView(
              builder: (context) {
                return const CupertinoPageScaffold(
                    navigationBar: CupertinoNavigationBar(
                      middle: Text('Settings'),
                    ),
                    child: Center(child: Text('Settings')));
              },
            );
            break;
        }
        return returnValue;
      },
    );
  }
}
