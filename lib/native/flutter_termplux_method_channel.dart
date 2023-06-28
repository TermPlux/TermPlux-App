import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_termplux_platform_interface.dart';

class MethodChannelFlutterTermPlux extends FlutterTermPluxPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_termplux');

  @override
  Future<bool?> getDynamicColors() async {
    final version = await methodChannel.invokeMethod<bool>('getDynamicColors');
    return version;
  }

  @override
  Future<String?> getShizukuVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getShizukuVersion');
    return version;
  }
}
