package io.ecosed.hybrid

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

internal class FlutterEcosed private constructor(): FlutterPlugin, MethodChannel.MethodCallHandler{

    private lateinit var mChannel: MethodChannel

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel = MethodChannel(binding.binaryMessenger, channelName)
        mChannel.setMethodCallHandler(this@FlutterEcosed)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "" -> result.success("")
            else -> result.notImplemented()
        }
    }

    companion object {
        const val channelName = "ecosed"

        fun build(): FlutterEcosed {
            return FlutterEcosed()
        }
    }
}