package io.ecosed.libecosed.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class AppsReceiver constructor(
    refresh: () -> Unit
) : BroadcastReceiver() {

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