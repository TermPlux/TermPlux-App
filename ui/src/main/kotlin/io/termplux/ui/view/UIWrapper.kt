package io.termplux.ui.view

import android.content.Context
import android.view.View

interface UIWrapper {
    fun content(context: Context): View
}