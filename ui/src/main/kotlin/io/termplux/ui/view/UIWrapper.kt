package io.termplux.ui.view

import android.app.Activity
import android.content.Context
import android.view.View

interface UIWrapper {
    fun content(
        context: Context,
        activity: Activity,
        container: View
    ): View
}