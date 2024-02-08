import 'package:flutter/material.dart';

import '../value/global.dart';
import 'info.dart';
import '../widget/loading_state.dart';

class LoadingPage extends StatelessWidget {
  const LoadingPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8),
      child: Column(
        children: [
          const Padding(
            padding: EdgeInsets.fromLTRB(16, 16, 16, 16),
            child: FractionallySizedBox(
              widthFactor: 0.4,
              child: FlutterLogo(),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 0, 0, 8),
            child: ValueListenableBuilder(
              valueListenable: Global.updateText,
              builder: (context, value, child) {
                return Text(
                  value,
                  textScaler: const TextScaler.linear(2),
                );
              },
            ),
          ),
          const FakeLoadingStatus(),
          // const Expanded(
          //   child: Padding(
          //     padding: EdgeInsets.all(8),
          //     child: Card(
          //       child: Padding(
          //         padding: EdgeInsets.all(8),
          //         child: Scrollbar(
          //           child: SingleChildScrollView(
          //             child: InfoPage(openFirstInfo: true),
          //           ),
          //         ),
          //       ),
          //     ),
          //   ),
          // )
        ],
      ),
    );
  }
}
