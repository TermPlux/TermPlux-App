package io.ecosed.libecosed.plugin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.preference.PreferenceManager
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.ecosed.libecosed.settings.EcosedSettings
import io.ecosed.plugin.LibEcosed
import io.ecosed.plugin.PluginBinding
import io.ecosed.plugin.PluginChannel

internal class LibEcosedPlugin : LibEcosed {

    private lateinit var mPluginChannel: PluginChannel

    private lateinit var mContext: Context


    private lateinit var mSharedPreferences: SharedPreferences

    override fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(
            binding = binding,
            channel = channel
        )
        mPluginChannel.getContext()?.let {
            mContext = it
        }


        mPluginChannel.setMethodCallHandler(handler = this@LibEcosedPlugin)
    }

    override fun onEcosedRemoved(binding: PluginBinding) {
        mPluginChannel.setMethodCallHandler(handler = null)
    }

    override fun initSDK(application: Application) {
        super.initSDK(application)

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

        DialogX.init(application)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.autoShowInputKeyboard = true
        DialogX.onlyOnePopTip = false
        DialogX.cancelable = true
        DialogX.cancelableTipDialog = false
        DialogX.bottomDialogNavbarColor = Color.Transparent.toArgb()
        DialogX.autoRunOnUIThread = true
        DialogX.useHaptic = true
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            getLaunchActivity -> result.success(mPluginChannel.getLaunchActivity(ecosed = this@LibEcosedPlugin))
            isDebug -> result.success(mPluginChannel.isDebug())
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    companion object {
        const val channel: String = "libecosed"
        internal const val getLaunchActivity: String = "launch_app"
        const val isDebug: String = "is_debug"
    }
}