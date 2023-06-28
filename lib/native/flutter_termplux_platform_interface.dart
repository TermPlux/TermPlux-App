
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_termplux_method_channel.dart';

abstract class FlutterTermPluxPlatform extends PlatformInterface {
  /// Constructs a FlutterTermpluxPlatform.
  FlutterTermPluxPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterTermPluxPlatform _instance = MethodChannelFlutterTermPlux();

  static FlutterTermPluxPlatform get instance => _instance;

  static set instance(FlutterTermPluxPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<bool?> getDynamicColors() {
    throw UnimplementedError('getDynamicColors() has not been implemented.');
  }

  Future<String?> getShizukuVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
