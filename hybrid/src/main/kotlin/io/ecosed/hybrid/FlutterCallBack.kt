package io.ecosed.hybrid

import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.engine.EcosedEngine
import io.ecosed.plugin.EcosedPlugin
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugins.GeneratedPluginRegistrant

internal class FlutterCallBack private constructor(plugin: FlutterPlugin) : FlutterBoost.Callback {

    private val mPlugin: FlutterPlugin = plugin
    private lateinit var mEngine: FlutterEngine

    override fun onStart(engine: FlutterEngine?) {
        engine?.let { flutterEngine: FlutterEngine ->
            mEngine = flutterEngine
            initPlugins()
        }
    }

    private fun initPlugins() {
        mEngine.plugins.add(mPlugin)
        GeneratedPluginRegistrant.registerWith(mEngine)
    }

    companion object {
        fun build(plugin: FlutterPlugin): FlutterCallBack {
            return FlutterCallBack(plugin = plugin)
        }
    }
}