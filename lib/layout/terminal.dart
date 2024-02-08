import 'package:flutter/material.dart';
import 'package:xterm/core.dart';
import 'package:xterm/ui.dart';

import '../value/default.dart';
import '../terminal/force_scale_gesture_recognizer.dart';
import '../value/global.dart';
import '../utils/util.dart';

class TerminalPage extends StatelessWidget {
  const TerminalPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          child: forceScaleGestureDetector(
            onScaleUpdate: (details) {
              Global.termFontScale.value =
                  (details.scale * (Util.getGlobal("termFontScale") as double))
                      .clamp(0.2, 5);
            },
            onScaleEnd: (details) async {
              await Global.prefs
                  .setDouble("termFontScale", Global.termFontScale.value);
            },
            child: ValueListenableBuilder(
              valueListenable: Global.termFontScale,
              builder: (context, value, child) {
                return TerminalView(
                  Global.termPtys[Global.currentContainer]!.terminal,
                  textScaleFactor: Global.termFontScale.value,
                  keyboardType: TextInputType.multiline,
                );
              },
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(8),
          child: Row(
            children: [
              AnimatedBuilder(
                animation: Global.keyboard,
                builder: (context, child) => ToggleButtons(
                  constraints:
                      const BoxConstraints(minWidth: 32, minHeight: 24),
                  tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                  borderRadius: const BorderRadius.all(Radius.circular(8)),
                  isSelected: [
                    Global.keyboard.ctrl,
                    Global.keyboard.alt,
                    Global.keyboard.shift
                  ],
                  onPressed: (index) {
                    switch (index) {
                      case 0:
                        Global.keyboard.ctrl = !Global.keyboard.ctrl;
                        break;
                      case 1:
                        Global.keyboard.alt = !Global.keyboard.alt;
                        break;
                      case 2:
                        Global.keyboard.shift = !Global.keyboard.shift;
                        break;
                    }
                  },
                  children: const [Text('Ctrl'), Text('Alt'), Text('Shift')],
                ),
              ),
              SizedBox.fromSize(size: const Size.square(8)),
              Expanded(
                child: SizedBox(
                  height: 24,
                  child: ListView.separated(
                    scrollDirection: Axis.horizontal,
                    itemBuilder: (context, index) {
                      return OutlinedButton(
                        style: Default.controlButtonStyle,
                        onPressed: () {
                          Global.termPtys[Global.currentContainer]!.terminal
                              .keyInput(Default.termCommands[index]["key"]!
                                  as TerminalKey);
                        },
                        child: Text(
                          Default.termCommands[index]["name"]! as String,
                        ),
                      );
                    },
                    separatorBuilder: (context, index) {
                      return SizedBox.fromSize(
                        size: const Size.square(4),
                      );
                    },
                    itemCount: Default.termCommands.length,
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
