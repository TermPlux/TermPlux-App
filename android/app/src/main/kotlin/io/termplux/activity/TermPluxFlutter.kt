package io.termplux.activity

import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.custom.LinkNativeViewFactory

class TermPluxFlutter : FlutterBoostActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        val registry = flutterEngine.platformViewsController.registry
        registry.registerViewFactory("android_view", LinkNativeViewFactory())

        val messenger = flutterEngine.dartExecutor.binaryMessenger
        val channel = MethodChannel(messenger, "termplux_channel")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {

                "" -> {}

                else -> {
                    res.error("error", "error_message", null)
                }
            }
        }
    }

    companion object {

    }
}