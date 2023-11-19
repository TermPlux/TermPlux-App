package io.ecosed.example

import io.ecosed.embedding.EcosedPlugin
import io.ecosed.embedding.EcosedMethodCall
import io.ecosed.embedding.EcosedResult

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