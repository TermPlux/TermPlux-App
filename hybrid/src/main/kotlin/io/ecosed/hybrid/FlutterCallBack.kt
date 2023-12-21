package io.ecosed.hybrid

import com.idlefish.flutterboost.FlutterBoost
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugins.GeneratedPluginRegistrant

internal class FlutterCallBack private constructor() : FlutterBoost.Callback {

    private lateinit var mPlugin: FlutterPlugin
    private lateinit var mEngine: FlutterEngine

    override fun onStart(engine: FlutterEngine?) {
        engine?.let { flutterEngine: FlutterEngine ->
            mEngine = flutterEngine
            mPlugin = FlutterEcosed.build()
            initPlugins()
        }
    }

    private fun initPlugins() {
        mEngine.plugins.add(mPlugin)
        GeneratedPluginRegistrant.registerWith(mEngine)
    }

    companion object {
        fun build(): FlutterCallBack {
            return FlutterCallBack()
        }
    }
}