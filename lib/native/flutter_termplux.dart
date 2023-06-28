import 'flutter_termplux_platform_interface.dart';

class FlutterTermPlux {
  Future<String?> getShizukuVersion() {
    return FlutterTermPluxPlatform.instance.getShizukuVersion();
  }

  Future<bool?> getDynamicColors() {
    return FlutterTermPluxPlatform.instance.getDynamicColors();
  }

  void toggle() {
    FlutterTermPluxPlatform.instance.toggle();
  }

}
