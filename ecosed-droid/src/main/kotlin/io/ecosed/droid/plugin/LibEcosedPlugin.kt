package io.ecosed.droid.plugin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import io.ecosed.droid.R
import io.ecosed.droid.settings.EcosedSettings
import org.lsposed.hiddenapibypass.HiddenApiBypass


internal class LibEcosedPlugin : LibEcosed() {

    private lateinit var mPluginChannel: PluginChannel

    override fun init() {

    }

    override fun initSDKs(application: Application) {
        super.initSDKs(application)

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
        }
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
//            getClient -> result.success(
//                mPluginChannel.getClient(
//                    ecosed = this@LibEcosedPlugin
//                )
//            )

            getMainFragment -> result.success(
//                mPluginChannel.getMainFragment(
//                    ecosed = this@LibEcosedPlugin
//                )
                null
            )

//            getProductLogo -> result.success(
//                mPluginChannel.getProductLogo(
//                    ecosed = this@LibEcosedPlugin
//                )
//            )

            isDebug -> result.success(
                mPluginChannel.isDebug()
            )

            else -> result.notImplemented()
        }
    }

    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    internal companion object {
        const val notificationChannel: String = "id"

        const val channel: String = "libecosed"
      //  const val getClient: String = "ecosed_client"
        const val getMainFragment: String = "fragment_main"
        //const val getProductLogo: String = "logo_product"
        const val isDebug: String = "is_debug"
    }
}