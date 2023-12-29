package io.termplux.framework

import android.app.Activity
import androidx.lifecycle.Lifecycle
import io.termplux.common.FlutterPluginProxy
import io.termplux.common.MethodCallProxy
import io.termplux.common.ResultProxy
import io.termplux.engine.EcosedEngine
import io.termplux.plugin.EcosedPlugin
import io.termplux.plugin.EcosedMethodCall
import io.termplux.plugin.EcosedResult

class Framework private constructor(): FlutterPluginProxy {

    private val engine = object : EcosedEngine() {

        override val hybridPlugin: EcosedPlugin
            get() = plugin
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

    override fun attach() {
        engine.attach()
    }

    companion object {
        const val channelName = "framework"

        fun build(): Framework {
            return Framework()
        }
    }
}