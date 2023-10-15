import 'package:flutter/widgets.dart';
import 'package:mpcore/mpcore.dart';

class MySecondPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MPScaffold(
      name: 'Second',
      body: Center(
        child: GestureDetector(
          onTap: () {
            if (Navigator.of(context).canPop()) {
              Navigator.of(context).pop();
            }
          },
          child: Container(
            width: 200,
            height: 200,
            color: Colors.pink,
            child: Center(
              child: Text(
                'Tap here',
                style: TextStyle(fontSize: 18, color: Colors.white),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
