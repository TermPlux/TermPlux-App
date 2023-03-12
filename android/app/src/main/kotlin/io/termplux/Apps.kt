package io.termplux

import android.graphics.drawable.Drawable

class Apps(
    var appIcon: Drawable,
    var appLabel: CharSequence,
    val pkgName: String
) {

    override fun toString(): String {
        return "Application{mAppIcon=$appIcon, mAppLabel=$appLabel}"
    }
}