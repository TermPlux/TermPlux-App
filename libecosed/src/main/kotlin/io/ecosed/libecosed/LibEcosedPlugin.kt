package io.ecosed.libecosed

import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedPluginBinding
import io.ecosed.plugin.EcosedPluginMethod
import io.ecosed.plugin.EcosedResult

class LibEcosedPlugin : EcosedPlugin {

    private lateinit var pluginMethod: EcosedPluginMethod

    override fun getEcosedPluginMethod(): EcosedPluginMethod = pluginMethod

    override fun onEcosedAttached(binding: EcosedPluginBinding) {
        pluginMethod = EcosedPluginMethod(binding = binding, channel = channel)
        pluginMethod.setMethodCallHandler(callBack = this@LibEcosedPlugin)
    }

    override fun onEcosedDetached(binding: EcosedPluginBinding) {
         pluginMethod.setMethodCallHandler(callBack = null)
    }

    override fun onEcosedMethodCall(call: String, result: EcosedResult) {
        when (call) {
            "" -> result.success("")
            else -> result.notImplemented()
        }
    }

    companion object {
        const val channel: String = "libecosed"
    }
}