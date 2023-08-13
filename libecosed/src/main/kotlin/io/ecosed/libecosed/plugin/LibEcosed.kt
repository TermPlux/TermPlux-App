package io.ecosed.libecosed.plugin

import android.content.Context
import io.ecosed.libecosed.client.EcosedBuilder
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedPluginBinding
import io.ecosed.plugin.EcosedPluginMethod
import io.ecosed.plugin.EcosedResult

internal class LibEcosed : EcosedPlugin {

    private lateinit var mPluginMethod: EcosedPluginMethod
    private lateinit var mContext: Context

    override fun getEcosedPluginMethod(): EcosedPluginMethod = mPluginMethod

    override fun onEcosedAttached(binding: EcosedPluginBinding) {
        mPluginMethod = EcosedPluginMethod(binding = binding, channel = channel)
        mContext = mPluginMethod.getActivity()
        mPluginMethod.setMethodCallHandler(callBack = this@LibEcosed)
    }

    override fun onEcosedDetached(binding: EcosedPluginBinding) {
        mPluginMethod.setMethodCallHandler(callBack = null)
    }

    override fun onEcosedMethodCall(call: String, result: EcosedResult) {
        EcosedBuilder().init(
            context = mContext
        ).build {
            when (call) {
                "" -> result.success("")
                else -> result.notImplemented()
            }
        }
    }

    companion object {
        const val channel: String = "libecosed"
    }
}