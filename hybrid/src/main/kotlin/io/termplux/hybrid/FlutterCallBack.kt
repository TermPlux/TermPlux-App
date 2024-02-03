package io.termplux.hybrid

import com.idlefish.flutterboost.FlutterBoost
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

internal class FlutterCallBack private constructor() : FlutterBoost.Callback {

    override fun onStart(engine: FlutterEngine?) {
        engine?.let { flutterEngine: FlutterEngine ->
            GeneratedPluginRegistrant.registerWith(flutterEngine)
        }
    }

    companion object {
        fun build(): FlutterCallBack {
            return FlutterCallBack()
        }
    }
}