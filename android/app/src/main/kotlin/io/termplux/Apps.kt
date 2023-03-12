package io.termplux

import android.content.Intent
import android.graphics.drawable.Drawable

class Apps(
    var appIcon: Drawable,
    var appLabel: CharSequence,
    var isSystemApp: Boolean,
    var appIntent: Intent,
    val pkgName: String
) {

    override fun toString(): String {
        return "Application{" +
                "mAppIcon=" + appIcon +
                ", mAppLabel=" + appLabel +
                ", mIsSystemApp=" + isSystemApp +
                ", mAppIntent=" + appIntent +
                '}'
    }
}