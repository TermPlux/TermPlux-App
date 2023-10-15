import 'package:flutter/widgets.dart';
import 'package:mpcore/mpcore.dart';

import 'second_page.dart';

void main() {
  runApp(MyApp());
  MPCore().connectToHostChannel();
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MPApp(
      title: 'TermPlux-App',
      color: Colors.blue,
      routes: {
        '/': (context) => MyHomePage(),
        '/second': (context) => MySecondPage(),
      },
      navigatorObservers: [MPCore.getNavigationObserver()],
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MPScaffold(
      name: 'TermPlux-App',
      // appBar: MPAppBar(context: context, title: Text('TermPlux-App')),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          _renderPushNextWidget(context),
          SizedBox(height: 8),
          _renderCallMPJSWidget(context),
        ],
      ),
    );
  }

  Widget _renderPushNextWidget(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed('/second');
      },
      child: Container(
        width: 200,
        height: 100,
        color: Colors.blue,
        child: Center(
          child: Text(
            'Hello, MPFlutter!',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: Colors.white,
            ),
          ),
        ),
      ),
    );
  }

  Widget _renderCallMPJSWidget(BuildContext context) {
    return GestureDetector(
      onTap: () async {
        final result = await MPJS.evalTemplate('foo', ['MPFlutter']);
        print(result);
      },
      child: Container(
        width: 200,
        height: 100,
        color: Colors.pink,
        child: Center(
          child: Text(
            'Hello, MPJS!',
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: Colors.white,
            ),
          ),
        ),
      ),
    );
  }
}
