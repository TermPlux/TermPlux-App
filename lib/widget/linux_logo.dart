import 'package:flutter/material.dart';

class LinuxSystemLogo extends StatelessWidget {
  const LinuxSystemLogo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final IconThemeData iconTheme = IconTheme.of(context);
    return SizedBox(
      width: iconTheme.size,
      child: const Image(image: AssetImage("assets/linux.png")),
    );
  }
}
