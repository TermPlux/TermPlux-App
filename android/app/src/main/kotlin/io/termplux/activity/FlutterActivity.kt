package io.termplux.activity

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.ui.flutter.LinkNativeViewFactory

class FlutterActivity : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        val registry = flutterEngine.platformViewsController.registry
        registry.registerViewFactory("android_view", LinkNativeViewFactory())

        val messenger = flutterEngine.dartExecutor.binaryMessenger
        val channel = MethodChannel(messenger, "android")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {
                "origin" -> {
                    //flutter.visibility = INVISIBLE

                    res.success("success")
                }
                else -> {
                    res.error("error", "error_message", null)
                }
            }
        }
    }

    private external fun fuck()

    companion object {

        init {
            System.loadLibrary("termplux")
        }
    }
}