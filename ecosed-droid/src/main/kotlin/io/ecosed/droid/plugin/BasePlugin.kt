package io.ecosed.droid.plugin

import android.content.Context
import android.content.ContextWrapper
import io.ecosed.droid.app.EcosedMethodCall
import io.ecosed.droid.app.EcosedResult

abstract class BasePlugin: ContextWrapper(null) {

    private lateinit var mPluginChannel: PluginChannel

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    internal fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(binding = binding, channel = channel)
        attachBaseContext(base =  mPluginChannel.getContext())
        mPluginChannel.setMethodCallHandler(handler = this@BasePlugin)
    }

    internal val getPluginChannel: PluginChannel
        get() = mPluginChannel

    abstract val channel: String

    open fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {

    }
}