import 'package:flutter/foundation.dart';

bool get kIsUseBoost {
  if (kIsWeb) return false;
  return [
    TargetPlatform.android,
    TargetPlatform.iOS,
  ].contains(defaultTargetPlatform);
}

bool get kIsUsePreview {
  if (kIsWeb) return true;
  return ![
    TargetPlatform.android,
    TargetPlatform.iOS,
  ].contains(defaultTargetPlatform);
}

bool get kIsDesktop {
  if (kIsWeb) return false;
  return [
    TargetPlatform.windows,
    TargetPlatform.linux,
    TargetPlatform.macOS,
  ].contains(defaultTargetPlatform);
}
