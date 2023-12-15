package io.ecosed

import android.app.Activity
import androidx.lifecycle.Lifecycle
import io.ecosed.common.FlutterPluginProxy
import io.ecosed.common.MethodCallProxy
import io.ecosed.common.ResultProxy
import io.ecosed.engine.EcosedEngine

class Framework : FlutterPluginProxy {

    private val engine = EcosedEngine.build()

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
}