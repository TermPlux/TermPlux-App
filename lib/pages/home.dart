import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../desktop/window_buttons.dart';
import '../desktop/window_move.dart';
import '../native/flutter_termplux.dart';
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

  String _platformVersion = 'Unknown';
  final _flutterTermpluxPlugin = FlutterTermPlux();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      platformVersion = await _flutterTermpluxPlugin.getShizukuVersion() ??
          'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

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

  void android() {
    navigation(context, '/android');
  }

  void ios(){

  }

  void windows() {

  }

  void macos() {

  }

  void linux(){

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("欢迎访问 ${widget.title}"),
        flexibleSpace: const WindowTitleBar(),
        actions: [

          IconButton(onPressed: navToPager, icon: const Icon(Icons.more_vert)),
          const WindowButtons()
        ],
      ),
      body: Center(
        child: Scrollbar(
          child: ListView(
            padding: const EdgeInsets.all(8),
            children: [
              Text(_platformVersion),
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
                pressed: android,
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
                pressed: ios,
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
                pressed: windows,
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
                pressed: macos,
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
                pressed: linux,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
