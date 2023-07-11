import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';

class RecordText extends StatelessWidget {
  const RecordText({super.key});

  Widget? record() {
    if (kIsWeb) {
      return const Center(
        child: Text("彤ICP备114514号 (这个是开玩笑的)"),
      );
    } else {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return SizedBox(child: record() ?? Container());
  }
}
