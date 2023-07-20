package io.termplux.framework.theme

import android.content.Context
import androidx.annotation.StyleRes;
import android.os.Build
import io.termplux.R
import io.termplux.framework.settings.TermPluxSettings
import io.termplux.framework.utils.EnvironmentUtils
import rikka.core.util.ResourceUtils

object ThemeHelper {
    private const val themeDefault = "DEFAULT"
    private const val themeBlack = "BLACK"


    private const val KEY_LIGHT_THEME = "light_theme"
    private const val KEY_BLACK_NIGHT_THEME = "black_night_theme"
    private const val KEY_USE_SYSTEM_COLOR = "use_system_color"

    fun isBlackNightTheme(context: Context): Boolean {
        return TermPluxSettings.getPreferences()
            .getBoolean(KEY_BLACK_NIGHT_THEME, EnvironmentUtils.isWatch(context))
    }

    fun isUsingSystemColor(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                TermPluxSettings.getPreferences().getBoolean(KEY_USE_SYSTEM_COLOR, true)
    }

    fun getTheme(context: Context): String {
        return if (isBlackNightTheme(context) && ResourceUtils.isNightMode(context.resources.configuration)) {
            themeBlack
        } else {
            TermPluxSettings.getPreferences().getString(KEY_LIGHT_THEME, themeBlack)!!
        }
    }

    @StyleRes
    fun getThemeStyleRes(context: Context): Int {
        return when (getTheme(context)) {
            themeBlack -> R.style.ThemeOverlay_Black
            themeDefault -> R.style.ThemeOverlay
            else -> R.style.ThemeOverlay
        }
    }
}