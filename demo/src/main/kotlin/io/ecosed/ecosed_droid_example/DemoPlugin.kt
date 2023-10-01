package io.ecosed.ecosed_droid_example

import io.ecosed.droid.app.EcosedPlugin
import io.ecosed.droid.app.EcosedMethodCall
import io.ecosed.droid.app.EcosedResult

class DemoPlugin : EcosedPlugin(channelName = mChannel) {

    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
        when (call.method) {
            "" -> result.success("")
            else -> result.notImplemented()
        }
    }

    companion object {
        const val mChannel: String = "demo_plugin"
    }
}