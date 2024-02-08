import 'package:flutter/material.dart';

import '../value/default.dart';
import '../value/global.dart';
import '../utils/util.dart';

class FastCommands extends StatefulWidget {
  const FastCommands({super.key});

  @override
  State<FastCommands> createState() => _FastCommandsState();
}

class _FastCommandsState extends State<FastCommands> {
  @override
  Widget build(BuildContext context) {
    return Wrap(
      alignment: WrapAlignment.center,
      spacing: 4.0,
      runSpacing: 4.0,
      children: Util.getCurrentProp("commands")
          .asMap()
          .entries
          .map<Widget>((e) {
        return OutlinedButton(
          style: Default.commandButtonStyle,
          child: Text(e.value["name"]!),
          onPressed: () {
            Util.termWrite(e.value["command"]!);
            Global.pageIndex.value = 0;
          },
          onLongPress: () {
            String name = e.value["name"]!;
            String command = e.value["command"]!;
            showDialog(
              context: context,
              builder: (context) {
                return AlertDialog(
                  title: const Text("指令编辑"),
                  content: SingleChildScrollView(
                    child: Column(
                      children: [
                        TextFormField(
                          initialValue: name,
                          decoration: const InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: "指令名称",
                          ),
                          onChanged: (value) {
                            name = value;
                          },
                        ),
                        SizedBox.fromSize(size: const Size.square(8)),
                        TextFormField(
                          maxLines: null,
                          initialValue: command,
                          decoration: const InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: "指令内容",
                          ),
                          onChanged: (value) {
                            command = value;
                          },
                        ),
                      ],
                    ),
                  ),
                  actions: [
                    TextButton(
                      onPressed: () async {
                        await Util.setCurrentProp("commands",
                            Util.getCurrentProp("commands")..removeAt(e.key));
                        setState(() {});
                        if (!context.mounted) return;
                        Navigator.of(context).pop();
                      },
                      child: const Text("删除该项"),
                    ),
                    TextButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      child: const Text("取消"),
                    ),
                    TextButton(
                      onPressed: () async {
                        await Util.setCurrentProp(
                            "commands",
                            Util.getCurrentProp("commands")
                              ..setAll(e.key, [
                                {"name": name, "command": command}
                              ]));
                        setState(() {});
                        if (!context.mounted) return;
                        Navigator.of(context).pop();
                      },
                      child: const Text("保存"),
                    ),
                  ],
                );
              },
            );
          },
        );
      }).toList()
        ..add(
          OutlinedButton(
            style: Default.commandButtonStyle,
            onPressed: () {
              String name = "";
              String command = "";
              showDialog(
                context: context,
                builder: (context) {
                  return AlertDialog(
                    title: const Text("指令编辑"),
                    content: SingleChildScrollView(
                      child: Column(
                        children: [
                          TextFormField(
                              initialValue: name,
                              decoration: const InputDecoration(
                                border: OutlineInputBorder(),
                                labelText: "指令名称",
                              ),
                              onChanged: (value) {
                                name = value;
                              }),
                          SizedBox.fromSize(size: const Size.square(8)),
                          TextFormField(
                            maxLines: null,
                            initialValue: command,
                            decoration: const InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: "指令内容",
                            ),
                            onChanged: (value) {
                              command = value;
                            },
                          ),
                        ],
                      ),
                    ),
                    actions: [
                      TextButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                          child: const Text("取消")),
                      TextButton(
                          onPressed: () async {
                            await Util.setCurrentProp(
                                "commands",
                                Util.getCurrentProp("commands")
                                  ..add({"name": name, "command": command}));
                            setState(() {});
                            if (!context.mounted) return;
                            Navigator.of(context).pop();
                          },
                          child: const Text("添加")),
                    ],
                  );
                },
              );
            },
            onLongPress: () {
              showDialog(
                context: context,
                builder: (context) {
                  return AlertDialog(
                    title: const Text("重置指令"),
                    content: const Text("是否重置所有快捷指令？"),
                    actions: [
                      TextButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                          child: const Text("取消")),
                      TextButton(
                          onPressed: () async {
                            await Util.setCurrentProp("commands", Default.commands);
                            setState(() {});
                            if (!context.mounted) return;
                            Navigator.of(context).pop();
                          },
                          child: const Text("是")),
                    ],
                  );
                },
              );
            },
            child: const Text("添加快捷指令"),
          ),
        ),
    );
  }
}
