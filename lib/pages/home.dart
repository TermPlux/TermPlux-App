import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';
import '../platform/platform.dart';
import '../navigation/navigation.dart';
import '../widget/image_logo.dart';
import '../widget/platform_card.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const channel = MethodChannel('termplux_channel');

  void navToPager() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        channel.invokeMethod("pager");
      }
    }
  }

  void toggle() {
    if (!kIsWeb) {
      if (Platform.isAndroid) {
        channel.invokeMethod("toggle");
      }
    }
  }

  void aaa() {
    navigation(context, '/');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('请选择目标平台'),
        flexibleSpace: WindowTitleBar(enable: kIsDesktop),
        actions: [
          IconButton(
              onPressed: navToPager, icon: const Icon(Icons.arrow_forward)),
          IconButton(onPressed: navToPager, icon: const Icon(Icons.more_vert)),
          WindowButtons(enable: kIsDesktop)
        ],
      ),
      body: Center(
        child: Scrollbar(
          child: ListView(
            padding: const EdgeInsets.only(top: 8, left: 8, right: 8),
            children: [
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/android.png"),
                  fit: BoxFit.cover,
                ),
                title: "Android",
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
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/ios.png"),
                  fit: BoxFit.cover,
                ),
                title: "iOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/windows.png"),
                  fit: BoxFit.cover,
                ),
                title: "Windows (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/windows.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("cover/macos.png"),
                  fit: BoxFit.cover,
                ),
                title: "macOS (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/apple.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              ),
              PlatformCard(
                cover: const Image(
                  image: AssetImage("assets/cover.jpg"),
                  fit: BoxFit.cover,
                ),
                title: "Linux (实验性)",
                icons: const [
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: ImageLogo(assets: 'icon/linux.png')),
                  Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 16, 16),
                      child: FlutterLogo()),
                ],
                pressed: aaa,
              )
            ],
          ),
        ),
      ),
    );
  }
}
