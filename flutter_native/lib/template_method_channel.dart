import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mp_flutter_runtime/mp_flutter_runtime.dart';

class TemplateMethodChannel extends MPMethodChannel {
  TemplateMethodChannel() : super('');

  //static MethodChannel methodChannel = const MethodChannel('example');

  @override
  Future? onMethodCall(String method, params) async {
    if (method == 'getDeviceName') {
      //await methodChannel.invokeMethod('method');
      return 'FlutterClient';
    }
    return super.onMethodCall(method, params);
  }
}

class TemplateFooView extends MPPlatformView {
  TemplateFooView({
    Key? key,
    Map? data,
    Map? parentData,
    required componentFactory,
  }) : super(
          key: key,
          data: data,
          parentData: parentData,
          componentFactory: componentFactory,
        );

  @override
  Future onMethodCall(String method, params) {
    // Handle method call here.
    return super.onMethodCall(method, params);
  }

  @override
  Widget builder(BuildContext context) {
    return Container(
      color: Colors.yellow,
    );
  }
}

void extMain() {
  MPPluginRegister.registerChannel(
    'com.mpflutter.templateMethodChannel',
    () => TemplateMethodChannel(),
  );
  MPPluginRegister.registerPlatformView(
    'com.mpflutter.templateFooView',
    (key, data, parentData, componentFactory) => TemplateFooView(
      key: key,
      data: data,
      parentData: parentData,
      componentFactory: componentFactory,
    ),
  );
}
