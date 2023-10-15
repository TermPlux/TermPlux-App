import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:mp_flutter_runtime/mp_flutter_runtime.dart';

import 'template_method_channel.dart';
import 'mp_config.dart';
import 'splash.dart';

void main() {
  extMain();
  CustomFlutterBinding();
  runApp(const MyAppPlay());
}

class CustomFlutterBinding extends WidgetsFlutterBinding
    with BoostFlutterBinding {}

class MyAppPlay extends StatelessWidget {
  const MyAppPlay({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  void open() {
    Navigator.push(context, MaterialPageRoute(builder: (context) {
      return const MPFlutterView(
          mode: MPMode.url,
          url: '${MPConfig.devServer}:9898',
          path: 'assets/app.mpk');
    }));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
              onPressed: open,
              child: const Text('open'),
            )
          ],
        ),
      ),
    );
  }
}

enum MPMode { url, file }

class MPFlutterView extends StatefulWidget {
  const MPFlutterView(
      {Key? key, required this.path, required this.mode, required this.url})
      : super(key: key);

  final MPMode mode;
  final String url;
  final String path;

  @override
  State<MPFlutterView> createState() => _MPFlutterViewState();
}

class _MPFlutterViewState extends State<MPFlutterView> {
  MPEngine? engine;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _initEngine();
  }

  void _initEngine() async {
    if (engine == null) {
      final engine = MPEngine(flutterContext: context);
      if (widget.mode == MPMode.url) {
        engine.initWithDebuggerServerAddr(widget.url);
      } else if (widget.mode == MPMode.file) {
        //engine.initWithMpkData((await rootBundle.load('assets/app.mpk')).buffer.asUint8List(),);
        engine.initWithMpkData(File(widget.path).readAsBytesSync());
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
    return MPPage(engine: engine!, splash: const Splash());
  }
}
