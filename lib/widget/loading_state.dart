import 'package:flutter/material.dart';

class FakeLoadingStatus extends StatefulWidget {
  const FakeLoadingStatus({super.key});

  @override
  State<FakeLoadingStatus> createState() => _FakeLoadingStatusState();
}

class _FakeLoadingStatusState extends State<FakeLoadingStatus> {
  @override
  Widget build(BuildContext context) {
    return const LinearProgressIndicator();
  }
}