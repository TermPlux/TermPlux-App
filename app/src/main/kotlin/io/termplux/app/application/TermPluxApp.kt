package io.termplux.app.application

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import io.flutter.app.FlutterApplication
import io.termplux.app.framework.settings.TermPluxSettings
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.material.app.LocaleDelegate

class TermPluxApp : FlutterApplication() {

    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initSharedPreferences()

        LocaleDelegate.defaultLocale = TermPluxSettings.getLocale()
        AppCompatDelegate.setDefaultNightMode(TermPluxSettings.getNightMode(context = this@TermPluxApp))

        initDynamicColors()
        initDialogX()
        initTaskbar()
    }

    /**
     * 启用隐藏接口
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    /**
     * 初始化SharedPreferences
     */
    private fun initSharedPreferences() {
        TermPluxSettings.initialize(context = this@TermPluxApp)
        mSharedPreferences = TermPluxSettings.getPreferences()
    }

    /**
     * 初始化动态颜色
     */
    private fun initDynamicColors() {
        if (mSharedPreferences.getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@TermPluxApp)
    }

    /**
     * 初始化DialogX
     */
    private fun initDialogX() {
        DialogX.init(this@TermPluxApp)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.autoShowInputKeyboard = true
        DialogX.onlyOnePopTip = false
        DialogX.cancelable = true
        DialogX.cancelableTipDialog = false
        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
        DialogX.autoRunOnUIThread = true
        DialogX.useHaptic = true
    }

    /**
     * 初始化Taskbar
     */
    private fun initTaskbar() {
        Taskbar.setEnabled(
            this@TermPluxApp,
            mSharedPreferences.getBoolean(
                "desktop",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )
    }
}