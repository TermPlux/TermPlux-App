package io.ecosed.droid.flutter

import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.droid.engine.EcosedEngine
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

internal class FlutterCallback private constructor() : FlutterBoost.Callback {

    private lateinit var mEngine: EcosedEngine

    override fun onStart(engine: FlutterEngine?) {
        engine?.let { flutterEngine ->
            flutterEngine.plugins.add(mEngine)
            GeneratedPluginRegistrant.registerWith(flutterEngine)
        }
    }

    internal companion object {
        internal fun build(engine: EcosedEngine): FlutterCallback {
            return FlutterCallback().apply {
                mEngine = engine
            }
        }
    }
}