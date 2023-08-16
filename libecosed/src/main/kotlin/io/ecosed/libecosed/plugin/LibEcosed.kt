package io.ecosed.libecosed.plugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.PluginBinding
import io.ecosed.plugin.PluginChannel

internal class LibEcosed : EcosedPlugin, PluginChannel.MethodCallHandler {

    private lateinit var mPluginChannel: PluginChannel

    private lateinit var mContext: Context
    private var isDebug: Boolean? = null
    private var mPackageName: String? = null
    private lateinit var mLaunchActivity: Activity

    override fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(binding = binding, channel = channel)
        mPluginChannel.getContext()?.let {
            mContext = it
        }
        mPluginChannel.isDebug().let {
            isDebug = it
        }
        mPluginChannel.getPackageName()?.let {
            mPackageName = it
        }
        mPluginChannel.getLaunchActivity()?.let {
            mLaunchActivity = it
        }
        mPluginChannel.setMethodCallHandler(handler = this@LibEcosed)
    }

    override fun onEcosedRemoved(binding: PluginBinding) {
        mPluginChannel.setMethodCallHandler(handler = null)
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            launchApp -> mContext.apply {
                val intent = Intent(this@apply, mLaunchActivity.javaClass)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    companion object {
        const val channel: String = "libecosed"
        const val launchApp: String = "launch_app"
    }
}