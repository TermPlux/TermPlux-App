package io.termplux.utils

import android.os.Build
import android.window.OnBackInvokedCallback
import androidx.annotation.RequiresApi
import com.kongzue.baseframework.BaseActivity
import java.lang.ref.WeakReference

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class BackInvokedCallbackUtils constructor(
    baseActivity: BaseActivity,
    backEvent: () -> Unit
) : OnBackInvokedCallback {

    private val activity: WeakReference<BaseActivity>
    private val onBackEvent: () -> Unit

    init {
        activity = WeakReference(baseActivity)
        onBackEvent = backEvent
    }

    override fun onBackInvoked() {
        activity.get()?.apply {
            onBackEvent()
        }
    }
}