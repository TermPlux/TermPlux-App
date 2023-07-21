package io.termplux.framework.base

import io.flutter.embedding.android.FlutterView

interface FlutterViewReturn {

    fun onFlutterCreated(flutterView: FlutterView?)
    fun onFlutterDestroy(flutterView: FlutterView?)

}