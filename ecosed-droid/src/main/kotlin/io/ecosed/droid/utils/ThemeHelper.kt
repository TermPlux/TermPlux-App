package io.ecosed.droid.utils

import android.content.Context
import androidx.annotation.StyleRes
import android.os.Build
import io.ecosed.droid.R
import io.ecosed.droid.settings.EcosedSettings


object ThemeHelper {
//    private const val themeDefault = "DEFAULT"
//    private const val themeBlack = "BLACK"
//
//
//    private const val KEY_LIGHT_THEME = "light_theme"
//
//    fun isBlackNightTheme(context: Context): Boolean {
//        return EcosedSettings.getPreferences()
//            .getBoolean(EcosedSettings.settingsBlackNight, EnvironmentUtils.isWatch(context))
//    }

    fun isUsingSystemColor(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                EcosedSettings.getPreferences().getBoolean(EcosedSettings.settingsDynamicColor, true)
    }

//    fun getTheme(context: Context): String {
//        return if (isBlackNightTheme(context) && ResourceUtils.isNightMode(context.resources.configuration)) {
//            themeBlack
//        } else {
//            EcosedSettings.getPreferences().getString(KEY_LIGHT_THEME, themeBlack)!!
//        }
//    }
//
//    @StyleRes
//    fun getThemeStyleRes(context: Context): Int {
//        return when (getTheme(context)) {
//            themeBlack -> R.style.ThemeOverlay_Black
//            themeDefault -> R.style.ThemeOverlay
//            else -> R.style.ThemeOverlay
//        }
//    }
}