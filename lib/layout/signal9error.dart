import 'package:clipboard/clipboard.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class Signal9Error extends StatelessWidget {
  const Signal9Error({super.key});

  @override
  Widget build(BuildContext context) {
    const TextStyle ts = TextStyle(
      fontSize: 16,
      color: Colors.white,
      fontWeight: FontWeight.normal,
    );
    const String helperLink = "https://www.vmos.cn/zhushou.htm";
    const String helperLink2 = "https://b23.tv/WwqOqW6";
    return Scaffold(
      backgroundColor: Colors.deepPurple,
      body: Center(
        child: Scrollbar(
          child: SingleChildScrollView(
            child: Column(
              children: [
                const Text(
                  ":(\n发生了什么？",
                  textScaler: TextScaler.linear(2),
                  style: ts,
                  textAlign: TextAlign.center,
                ),
                const Text(
                  "终端异常退出, 返回错误码9\n此错误通常是高版本安卓系统(12+)限制进程造成的, \n可以使用以下工具修复:",
                  style: ts,
                  textAlign: TextAlign.center,
                ),
                const SelectableText(
                  helperLink,
                  style: ts,
                  textAlign: TextAlign.center,
                ),
                const Text(
                  "(复制链接到浏览器查看)",
                  style: ts,
                  textAlign: TextAlign.center,
                ),
                OutlinedButton(
                  onPressed: () {
                    FlutterClipboard.copy(helperLink).then(
                      (value) {
                        ScaffoldMessenger.of(context).hideCurrentSnackBar();
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: const Text("已复制"),
                            action: SnackBarAction(
                              label: "跳转",
                              onPressed: () {
                                launchUrl(
                                  Uri.parse(helperLink),
                                  mode: LaunchMode.externalApplication,
                                );
                              },
                            ),
                          ),
                        );
                      },
                    );
                  },
                  child: const Text(
                    "复制",
                    style: ts,
                    textAlign: TextAlign.center,
                  ),
                ),
                const Text(
                  "如果不能解决请参考此教程: ",
                  style: ts,
                  textAlign: TextAlign.center,
                ),
                OutlinedButton(
                  onPressed: () {
                    FlutterClipboard.copy(helperLink2).then(
                      (value) {
                        ScaffoldMessenger.of(context).hideCurrentSnackBar();
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: const Text("已复制"),
                            action: SnackBarAction(
                              label: "跳转",
                              onPressed: () {
                                launchUrl(
                                  Uri.parse(helperLink2),
                                  mode: LaunchMode.externalApplication,
                                );
                              },
                            ),
                          ),
                        );
                      },
                    );
                  },
                  child: const Text(
                    "查看",
                    style: ts,
                    textAlign: TextAlign.center,
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
