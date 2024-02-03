package io.termplux.app

import com.google.android.material.color.DynamicColors
import io.termplux.base.TPBaseApplication

class TermPluxApp : TPBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(
            this@TermPluxApp
        )
    }
}