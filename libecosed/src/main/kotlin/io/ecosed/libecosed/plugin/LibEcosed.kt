package io.ecosed.libecosed.plugin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import io.ecosed.libecosed.settings.EcosedSettings
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.PluginBinding
import io.ecosed.plugin.PluginChannel

internal class LibEcosed : EcosedPlugin, PluginChannel.MethodCallHandler {

    private lateinit var mPluginChannel: PluginChannel

    private lateinit var mContext: Context
    private var mDebug: Boolean? = null
    private var mPackageName: String? = null
    private lateinit var mLaunchActivity: Activity

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(
            binding = binding,
            channel = channel
        )
        mPluginChannel.getContext()?.let {
            mContext = it
        }
        mPluginChannel.isDebug().let {
            mDebug = it
        }
        mPluginChannel.getPackageName(plugin = this@LibEcosed)?.let {
            mPackageName = it
        }
        mPluginChannel.getLaunchActivity(plugin = this@LibEcosed)?.let {
            mLaunchActivity = it
        }
        mPluginChannel.setMethodCallHandler(handler = this@LibEcosed)
    }

    override fun onEcosedRemoved(binding: PluginBinding) {
        mPluginChannel.setMethodCallHandler(handler = null)
    }

    override fun initSDK(application: Application) {
        super.initSDK(application)
        Log.d("tag", "sdk已加载")
        EcosedSettings.initialize(context = application)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        Taskbar.setEnabled(
            application,
            mSharedPreferences.getBoolean(
                "desktop",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )
        if (mSharedPreferences.getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(application)

//        DialogX.init(application)
//        DialogX.globalStyle = IOSStyle()
//        DialogX.globalTheme = DialogX.THEME.AUTO
//        DialogX.autoShowInputKeyboard = true
//        DialogX.onlyOnePopTip = false
//        DialogX.cancelable = true
//        DialogX.cancelableTipDialog = false
//        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
//        DialogX.autoRunOnUIThread = true
//        DialogX.useHaptic = true
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            getPackage -> result.success(mPackageName)
            launchApp -> mContext.apply {
                val intent = Intent(this@apply, mLaunchActivity.javaClass)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            isDebug -> result.success(mDebug)
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    companion object {
        const val channel: String = "libecosed"
        const val launchApp: String = "launch_app"
        const val getPackage: String = "get_package"
        const val isDebug: String = "is_debug"
    }
}