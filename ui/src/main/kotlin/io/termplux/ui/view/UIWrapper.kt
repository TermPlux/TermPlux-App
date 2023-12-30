package io.termplux.ui.view

import android.content.Context
import android.view.View

interface UIWrapper {
    fun launcher(context: Context): View
    fun stub(context: Context): View
    fun manager(context: Context): View
}