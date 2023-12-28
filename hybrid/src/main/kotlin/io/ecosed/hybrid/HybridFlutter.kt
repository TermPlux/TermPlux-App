package io.ecosed.hybrid

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.common.FlutterPluginProxy
import io.ecosed.common.MethodCallProxy
import io.ecosed.common.ResultProxy
import io.ecosed.framework.Framework
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.FlutterLifecycleAdapter
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class HybridFlutter : ContextWrapper(null), HybridWrapper {

    private lateinit var mApplication: Application
    private lateinit var mMethodChannel: MethodChannel

    private val mFlutter: FlutterPlugin = object : FlutterPlugin, MethodChannel.MethodCallHandler,
        ActivityAware {

        override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
            mMethodChannel = MethodChannel(binding.binaryMessenger, channelName)
            mMethodChannel.setMethodCallHandler(this)
        }

        override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
            mMethodChannel.setMethodCallHandler(null)
        }

        override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) = engineUnit {
            onMethodCall(
                call = object : MethodCallProxy {

                    override val methodProxy: String?
                        get() = call.method

                    override val bundleProxy: Bundle?
                        get() {

                            return null
                        }
                },
                result = object : ResultProxy {
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
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun withApplication(application: Application): HybridWrapper {
        return this@HybridFlutter.apply {
            mApplication = application
        }
    }

    override fun build(): HybridFlutter {
        return this@HybridFlutter.apply {
            attachBaseContext(
                base = mApplication.baseContext
            )
            FlutterBoost.instance().setup(
                mApplication,
                FlutterDelegate.build(),
                FlutterCallBack.build(
                    plugin = mFlutter
                )
            )
        }
    }

    private fun engineUnit(
        content: FlutterPluginProxy.() -> Unit,
    ) {
        content.invoke(Framework.build())
    }

    companion object {
        const val channelName: String = "hybrid_flutter"

        fun build(): HybridFlutter {
            return HybridFlutter()
        }
    }
}