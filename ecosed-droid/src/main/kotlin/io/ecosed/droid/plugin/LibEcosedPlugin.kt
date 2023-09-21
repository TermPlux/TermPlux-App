package io.ecosed.droid.plugin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.ecosed.droid.R
import io.ecosed.droid.settings.EcosedSettings
import org.lsposed.hiddenapibypass.HiddenApiBypass


internal class LibEcosedPlugin : LibEcosed() {

    private lateinit var mPluginChannel: PluginChannel

    override fun init() {

    }

    override fun initSDKs(application: Application) {
        super.initSDKs(application)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
        // 创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                notificationChannel,
                application.getString(R.string.lib_name),
                importance
            ).apply {
                description = application.getString(R.string.lib_description)
            }
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // 初始化首选项
        EcosedSettings.initialize(context = application)
        // 初始化动态取色
        if (EcosedSettings.getPreferences().getBoolean(
                EcosedSettings.settingsDynamicColor,
                true
            ) and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        ) {
            DynamicColors.applyToActivitiesIfAvailable(
                application,
                DynamicColorsOptions.Builder().build()
            )
//            HarmonizedColors.applyToContextIfAvailable(
//                application,
//                HarmonizedColorsOptions.Builder()
//                    .setColorAttributeToHarmonizeWith(android.R.attr.colorPrimary)
//                    .setColorAttributes(
//                        HarmonizedColorAttributes.create(
//                            HarmonizedColorAttributes.createMaterialDefaults().attributes
//                        )
//                    )
//                    .build()
//            )
        }
//        // 初始化Weex
//        WXBridgeManager.updateGlobalConfig("wson_on")
//        WXEnvironment.setOpenDebugLog(true)
//        WXEnvironment.setApkDebugable(true)
//        WXSDKEngine.addCustomOptions("appName", "WXSample")
//        WXSDKEngine.addCustomOptions("appGroup", "WXApp")
//        val builder = InitConfig.Builder()
//        WXSDKEngine.initialize(application, builder.build())
        // 初始化任务栏
        Taskbar.setEnabled(
            application,
            EcosedSettings.getPreferences().getBoolean(
                EcosedSettings.settingsDesktop,
                true
            ) and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        )


        // 弃用
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

    override fun initSDKInitialized() {
        super.initSDKInitialized()

    }

    override fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(binding = binding, channel = channel)
        mPluginChannel.setMethodCallHandler(handler = this@LibEcosedPlugin)
    }

    override fun onEcosedMethodCall(call: PluginChannel.MethodCall, result: PluginChannel.Result) {
        when (call.method) {
            getClient -> result.success(
                mPluginChannel.getClient(
                    ecosed = this@LibEcosedPlugin
                )
            )
            getMainFragment -> result.success(
//                mPluginChannel.getMainFragment(
//                    ecosed = this@LibEcosedPlugin
//                )
                null
            )
            getProductLogo -> result.success(
                mPluginChannel.getProductLogo(
                    ecosed = this@LibEcosedPlugin
                )
            )
            isDebug -> result.success(
                mPluginChannel.isDebug()
            )
            getWallpaper -> result.success(
                ContextCompat.getDrawable(
                    mPluginChannel.getContext()!!,
                    R.drawable.custom_wallpaper_24
                )
            )
            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    internal companion object {
        const val notificationChannel: String = "id"

        const val channel: String = "libecosed"
        const val getClient: String = "ecosed_client"
        const val getMainFragment: String = "fragment_main"
        const val getProductLogo: String = "logo_product"
        const val isDebug: String = "is_debug"
        const val getWallpaper: String = "wallpaper_drawable"
    }
}