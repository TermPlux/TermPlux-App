import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_termplux_platform_interface.dart';

class MethodChannelFlutterTermPlux extends FlutterTermPluxPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_termplux');

  @override
  Future<bool?> getDynamicColors() async {
    if (Platform.isAndroid) {
      final dynamic =
          await methodChannel.invokeMethod<bool>('getDynamicColors');
      return dynamic;
    } else {
      return true;
    }
  }

  @override
  Future<String?> getShizukuVersion() async {
    if (Platform.isAndroid) {
      final version =
          await methodChannel.invokeMethod<String>('getShizukuVersion');
      return version;
    } else {
      return "getShizukuVersion: 此平台不支持此接口";
    }
  }

  @override
  void toggle() {
    if (Platform.isAndroid) methodChannel.invokeMethod<void>("toggle");
  }
}
