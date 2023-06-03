package io.termplux.utils

import android.os.Build
import android.window.OnBackInvokedCallback
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class BackInvokedCallbackUtils constructor(
    backEvent: () -> Unit
) : OnBackInvokedCallback {

    private val onBackEvent: () -> Unit

    init {
        onBackEvent = backEvent
    }

    override fun onBackInvoked() = onBackEvent()
}