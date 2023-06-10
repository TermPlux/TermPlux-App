import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import '../platform/platform.dart';

void navigation(BuildContext context, String route) {
  if (kIsUseBoost) {
    BoostNavigator.instance.push(route, withContainer: true);
  } else {
    Navigator.pushNamed(context, route);
  }
}
