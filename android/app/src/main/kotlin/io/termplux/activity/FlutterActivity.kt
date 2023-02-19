package io.termplux.activity

import io.flutter.embedding.android.FlutterActivity

class FlutterActivity : FlutterActivity() {

    companion object {

        init {
            System.loadLibrary("termplux")
        }
    }
}