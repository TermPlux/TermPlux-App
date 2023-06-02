import 'package:flutter/material.dart';

class ImageLogo extends StatelessWidget {
  const ImageLogo({super.key, required this.assets});

  final String assets;

  @override
  Widget build(BuildContext context) {
    final IconThemeData iconTheme = IconTheme.of(context);
    return SizedBox(
      width: iconTheme.size,
      child: Image(image: AssetImage(assets))
    );
  }
}
