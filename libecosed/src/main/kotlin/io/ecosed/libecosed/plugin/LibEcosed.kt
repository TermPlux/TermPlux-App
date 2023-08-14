package io.ecosed.libecosed.plugin

import android.content.Context
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.PluginChannel

internal class LibEcosed : EcosedPlugin, PluginChannel.MethodCallHandler {

    private lateinit var mPluginChannel: PluginChannel
    private lateinit var mContext: Context

    override fun onEcosedAdded(binding: EcosedPlugin.EcosedPluginBinding) {
        mPluginChannel = PluginChannel(binding = binding, channel = channel)
        mContext = mPluginChannel.getContext()
        mPluginChannel.setMethodCallHandler(handler = this@LibEcosed)
    }

    override fun onEcosedRemoved(binding: EcosedPlugin.EcosedPluginBinding) {
        mPluginChannel.setMethodCallHandler(handler = null)
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            "text" -> result.success("Hello World!")
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    companion object {
        const val channel: String = "libecosed"
    }
}