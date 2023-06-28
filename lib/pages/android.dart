import 'package:flutter/material.dart';

class AndroidPlatformPage extends StatefulWidget {
  const AndroidPlatformPage({super.key});

  @override
  State<AndroidPlatformPage> createState() => _AndroidPlatformPageState();
}

class _AndroidPlatformPageState extends State<AndroidPlatformPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Android"),
      ),
      body: Center(
        child: Text("6"),
      ),
    );
  }
}
