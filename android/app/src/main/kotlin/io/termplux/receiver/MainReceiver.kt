package io.termplux.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MainReceiver(refresh: () -> Unit) : BroadcastReceiver() {

    private val mRefreshes : () -> Unit

    init {
        mRefreshes = refresh
    }

    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {
        mRefreshes()
    }
}