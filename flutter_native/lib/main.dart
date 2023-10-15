import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mp_flutter_runtime/mp_flutter_runtime.dart';

import 'ext/template_method_channel.dart';
import 'mp_config.dart';
import 'splash.dart';

void main() {
  extMain();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MPFlutter Template',
      theme: ThemeData(useMaterial3: true),
      home: const MPFlutterContainerPage(),
    );
  }
}

class MPFlutterContainerPage extends StatefulWidget {
  const MPFlutterContainerPage({Key? key}) : super(key: key);

  @override
  State<MPFlutterContainerPage> createState() => _MPFlutterContainerPageState();
}

class _MPFlutterContainerPageState extends State<MPFlutterContainerPage> {
  MPEngine? engine;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    initEngine();
  }

  void initEngine() async {
    if (engine == null) {
      final engine = MPEngine(flutterContext: context);
      if (MPConfig.dev) {
        engine.initWithDebuggerServerAddr('${MPConfig.devServer}:9898');
      } else {
        engine.initWithMpkData(
          (await rootBundle.load('assets/app.mpk')).buffer.asUint8List(),
        );
      }
      setState(() {
        this.engine = engine;
      });
      await Future.delayed(const Duration(milliseconds: 100));
      await engine.start();
    }
  }

  @override
  Widget build(BuildContext context) {
    if (engine == null) return const Splash();
    return MPPage(engine: engine!);
  }
}
