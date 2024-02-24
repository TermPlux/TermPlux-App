import 'package:flutter/material.dart';

import '../widget/command.dart';
import 'info.dart';
import 'settings.dart';

class Contor extends StatelessWidget {
  const Contor({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8),
      child: Scrollbar(
        child: SingleChildScrollView(
          restorationId: "control-scroll",
          child: Column(
            children: [
              const Padding(
                padding: EdgeInsets.all(16),
                child: FractionallySizedBox(
                  widthFactor: 0.4,
                  child: FlutterLogo(),
                ),
              ),
              const FastCommands(),
              Padding(
                padding: const EdgeInsets.all(8),
                child: Card(
                  child: Padding(
                    padding: const EdgeInsets.all(8),
                    child: Column(
                      children: [
                        const SettingPage(),
                        SizedBox.fromSize(size: const Size.square(8)),
                        const InfoPage(
                          openFirstInfo: false,
                        )
                      ],
                    ),
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
