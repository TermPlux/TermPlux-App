package io.termplux.hybrid

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.FlutterLifecycleAdapter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.termplux.common.FlutterPluginProxy
import io.termplux.framework.Framework

class EnginePlugin private constructor(): FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

    private lateinit var mMethodChannel: MethodChannel

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mMethodChannel = MethodChannel(binding.binaryMessenger, channelName)
        mMethodChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        mMethodChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) = engineUnit {
        onMethodCall(
            call = object : io.termplux.common.MethodCallProxy {

                override val methodProxy: String?
                    get() = call.method

                override val bundleProxy: android.os.Bundle?
                    get() {

                        return null
                    }
            },
            result = object : io.termplux.common.ResultProxy {
                override fun success(
                    resultProxy: Any?,
                ) = result.success(
                    resultProxy
                )

                override fun error(
                    errorCodeProxy: String,
                    errorMessageProxy: String?,
                    errorDetailsProxy: Any?,
                ) = result.error(
                    errorCodeProxy,
                    errorMessageProxy,
                    errorDetailsProxy
                )

                override fun notImplemented() = result.notImplemented()
            }
        )
    }

    override fun onAttachedToActivity(
        binding: ActivityPluginBinding,
    ) = engineUnit {
        getActivity(
            activity = binding.activity
        )
        getLifecycle(
            lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding)
        )
        attach()
    }

    override fun onDetachedFromActivityForConfigChanges() = Unit

    override fun onReattachedToActivityForConfigChanges(
        binding: ActivityPluginBinding,
    ) = engineUnit {
        getActivity(
            activity = binding.activity
        )
        getLifecycle(
            lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding)
        )
    }

    override fun onDetachedFromActivity() = Unit

    private fun engineUnit(
        content: FlutterPluginProxy.() -> Unit,
    ) {
        content.invoke(Framework.build())
    }

    companion object {
        const val channelName: String = "hybrid_flutter"

        fun build(): EnginePlugin {
            return EnginePlugin()
        }
    }
}