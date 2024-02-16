import 'package:flutter/material.dart';
import 'package:flutter_pty/flutter_pty.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:webview_flutter/webview_flutter.dart';

import '../terminal/term_pty.dart';
import '../terminal/virtual_keyboard.dart';

// Global variables
class Global {
  static late String dataPath;
  static Pty? audioPty;
  static late WebViewController controller;
  //static late BuildContext homePageStateContext;

  //目前运行第几个容器
  static late int currentContainer;

  //为容器<int>存放TermPty数据
  static late Map<int, TermPty> termPtys;

  //存储ctrl, shift, alt状态
  static late VirtualKeyboard keyboard;

  //为了区分按下的ctrl+J和enter而准备的变量
  static bool maybeCtrlJ = false;

  //终端字体大小，存储为G.prefs的termFontScale
  static ValueNotifier<double> termFontScale = ValueNotifier(1);

  static bool isStreamServerStarted = false;
  static bool isStreaming = false;

  //static int? streamingPid;
  static String streamingOutput = "";
  static late Pty streamServerPty;
  static bool isVirglServerStarted = false;
  static late Pty virglServerPty;

  //static int? virglPid;
  //主界面索引
  static ValueNotifier<int> screenIndex = ValueNotifier(0);
  static bool complete = false;

  //更改值，用于刷新启动命令
  static ValueNotifier<bool> bootTextChange = ValueNotifier(true);

  //加载界面的说明文字
  static ValueNotifier<String> updateText = ValueNotifier("proot");

  //本次启动时是否启用了box86/64
  static bool wasBoxEnabled = false;

  //本次启动时是否启用了wine
  static bool wasWineEnabled = false;

  static late SharedPreferences prefs;
}
