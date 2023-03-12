package io.termplux.activity

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class FlutterActivity: FlutterActivity(){

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
    }

    companion object{

        init {
            System.loadLibrary("termplux")
        }
    }
}