import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:termplux/widget/record_text.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';
import '../native/flutter_termplux.dart';
import '../navigation/navigation.dart';
import '../widget/image_logo.dart';
import '../widget/platform_card.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // String _platformVersion = 'Unknown';
  final _flutterTermpluxPlugin = FlutterTermPlux();

  @override
  void initState() {
    super.initState();
    //initPlatformState();
  }

  // Future<void> initPlatformState() async {
  //   String platformVersion;

  //   try {
  //     platformVersion = await _flutterTermpluxPlugin.getShizukuVersion() ??
  //         'Unknown platform version';
  //   } on PlatformException {
  //     platformVersion = 'Failed to get platform version.';
  //   }

  //   if (!mounted) return;

  //   setState(() {
  //     _platformVersion = platformVersion;
  //   });
  // }

  void toggle() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        _flutterTermpluxPlugin.toggle();
      }
    }
  }

  void android() {
    navigation(context, '/android');
  }

  void ios() {
    navigation(context, '/ios');
  }

  void windows() {
    navigation(context, '/windows');
  }

  void macos() {
    navigation(context, '/macos');
  }

  void linux() {
    navigation(context, '/linux');
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoTabScaffold(
      tabBar: CupertinoTabBar(
        currentIndex: 0,
        items: const <BottomNavigationBarItem>[
          // 3 <-- SEE HERE
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
          // 4 <-- SEE HERE
            returnValue = CupertinoTabView(builder: (context) {
              return CupertinoPageScaffold(
                navigationBar: CupertinoNavigationBar(
                  middle: Text('Calls'),
                ),
                child: PlatformCard(
                  cover: const Image(
                    image: AssetImage("cover/android.png"),
                    fit: BoxFit.cover,
                  ),
                  title: "Google - Android",
                  icons: const [
                    Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                        child: ImageLogo(assets: 'icon/android.png')),
                    Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                        child: FlutterLogo()),
                    Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                        child: ImageLogo(assets: 'icon/compose.png')),
                    Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                        child: ImageLogo(assets: 'icon/unity.png')),
                  ],
                  pressed: android,
                )
              );
            });
            break;
          case 1:
            returnValue = CupertinoTabView(
              builder: (context) {
                return Container();
              },
            );
            break;
          case 2:
            returnValue = CupertinoTabView(
              builder: (context) {
                return Container();
              },
            );
            break;
        }
        return returnValue;
      },

    );




    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(),
      child: Center(
        child: Scrollbar(
          child: ListView(
            padding: const EdgeInsets.all(8),
            children: [
              //   Text(_platformVersion),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/android.png"),
                  fit: BoxFit.cover,
                ),
                title: "Google - Android",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/android.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/compose.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/unity.png')),
                ],
                pressed: android,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/ios.png"),
                  fit: BoxFit.cover,
                ),
                title: "Apple - iOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: ios,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/windows.png"),
                  fit: BoxFit.cover,
                ),
                title: "Microsoft - Windows (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/windows.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: windows,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/macos.png"),
                  fit: BoxFit.cover,
                ),
                title: "Apple - macOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: macos,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "GNU/Linux (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/linux.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: linux,
              ),
              const RecordText()
            ],
          ),
        ),
      ),

    );
    return Scaffold(
      appBar: AppBar(
        title: const Text("欢迎访问 - 选择目标平台"),
        flexibleSpace: const WindowTitleBar(),
        actions: [
          IconButton(onPressed: toggle, icon: const Icon(Icons.more_vert)),
          const WindowButtons()
        ],
      ),
      body: Center(
        child: Scrollbar(
          child: ListView(
            padding: const EdgeInsets.all(8),
            children: [
              //   Text(_platformVersion),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/android.png"),
                  fit: BoxFit.cover,
                ),
                title: "Google - Android",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/android.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/compose.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/unity.png')),
                ],
                pressed: android,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/ios.png"),
                  fit: BoxFit.cover,
                ),
                title: "Apple - iOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: ios,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/windows.png"),
                  fit: BoxFit.cover,
                ),
                title: "Microsoft - Windows (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/windows.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: windows,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/macos.png"),
                  fit: BoxFit.cover,
                ),
                title: "Apple - macOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: macos,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "GNU/Linux (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/linux.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: linux,
              ),
              const RecordText()
            ],
          ),
        ),
      ),
    );
  }
}
