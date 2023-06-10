package io.termplux.utils

import android.os.Build
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.lifecycle.LifecycleObserver
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import java.lang.ref.WeakReference

class LifeCircleUtils constructor(
    baseActivity: BaseActivity,
    observer: LifecycleObserver
) : LifeCircleListener() {

    private val mActivity: WeakReference<BaseActivity>
    private val mObserver: LifecycleObserver

    private lateinit var onBackInvokedCallback: OnBackInvokedCallback

    init {
        mActivity = WeakReference(baseActivity)
        mObserver = observer
    }

    override fun onCreate() {
        super.onCreate()
        mActivity.get()?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                onBackInvokedCallback = BackInvokedCallbackUtils {
                    onBack()
                }.also {
                    onBackInvokedDispatcher.registerOnBackInvokedCallback(
                        OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                        it
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.get()?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                onBackInvokedCallback.let {
                    onBackInvokedDispatcher.unregisterOnBackInvokedCallback(it)
                }
            }
            lifecycle.removeObserver(mObserver)
        }
    }
}