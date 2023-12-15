package io.ecosed

import android.app.Activity
import androidx.lifecycle.Lifecycle
import io.ecosed.common.FlutterPluginProxy
import io.ecosed.common.MethodCallProxy
import io.ecosed.common.ResultProxy
import io.ecosed.engine.EcosedEngine
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedMethodCall
import io.ecosed.plugin.EcosedResult

class Framework : FlutterPluginProxy {

    private val engine = object : EcosedEngine() {

        override fun attach() {
            super.attach()
        }

        override fun getActivity(activity: Activity) {
            super.getActivity(activity)
        }

        override fun getLifecycle(lifecycle: Lifecycle) {
            super.getLifecycle(lifecycle)
        }

        override fun onMethodCall(call: MethodCallProxy, result: ResultProxy) {
            super.onMethodCall(call, result)
        }
    }

    private val plugin = object : EcosedPlugin() {

        override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
            super.onEcosedMethodCall(call, result)
            when(call.method) {
                "" -> result.success("")
                else -> result.notImplemented()
            }
        }

        override val channel: String
            get() = channelName
    }

    override fun getActivity(activity: Activity) {
        engine.getActivity(activity = activity)
    }

    override fun getLifecycle(lifecycle: Lifecycle) {
        engine.getLifecycle(lifecycle = lifecycle)
    }

    override fun onMethodCall(call: MethodCallProxy, result: ResultProxy) {
        engine.onMethodCall(call = call, result = result)
    }

    fun attach() {
        engine.attach()
    }

    companion object {
        const val channelName = "framework"
    }
}