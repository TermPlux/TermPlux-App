package io.ecosed.ecosed_droid_example

import io.ecosed.droid.app.EcosedPlugin
import io.ecosed.droid.app.PluginBinding
import io.ecosed.droid.app.PluginChannel

class DemoPlugin : EcosedPlugin, PluginChannel.MethodCallHandler {

    private lateinit var pluginChannel: PluginChannel

    override fun onEcosedAdded(binding: PluginBinding) {
        pluginChannel = PluginChannel(binding = binding, channel = channel)
        pluginChannel.setMethodCallHandler(handler = this@DemoPlugin)
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            "" -> result.success("")
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = pluginChannel

    companion object {
        const val channel: String = "my_plugin"
    }
}