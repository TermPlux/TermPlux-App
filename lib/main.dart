import 'package:flutter/material.dart';

import 'app.dart';
import 'boost.dart';

void main() {
  ///这里的CustomFlutterBinding调用务必不可缺少，用于控制Boost状态的resume和pause
  CustomFlutterBinding();
  runApp(const TermPluxApp());
}