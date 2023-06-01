import 'package:flutter/material.dart';

class ChromeBrowserLogo extends StatelessWidget {
  const ChromeBrowserLogo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final IconThemeData iconTheme = IconTheme.of(context);
    return SizedBox(
      width: iconTheme.size,
      child: const Image(image: AssetImage("assets/chrome.png")),
    );
  }
}
