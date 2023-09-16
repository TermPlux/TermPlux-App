package io.ecosed.droid.app

import android.graphics.Color
import androidx.annotation.ColorInt

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NavigationBarBackgroundColor(
    @ColorInt val color: Int = Color.TRANSPARENT
)