package io.termplux.app.framework.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration

object EnvironmentUtils {

    fun isWatch(context: Context): Boolean {
        return (context.getSystemService(UiModeManager::class.java).currentModeType == Configuration.UI_MODE_TYPE_WATCH)
    }

}