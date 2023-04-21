package io.termplux.basic.custom

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.webkit.WebView
import com.kongzue.dialogx.interfaces.ScrollController

class ScrollControllerWebView constructor(
    context: Context
) : WebView(
    context
), ScrollController {

    private var lockScroll = false

    override fun isLockScroll(): Boolean {
        return lockScroll
    }

    override fun lockScroll(lockScroll: Boolean) {
        this.lockScroll = lockScroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (lockScroll) false else super.onTouchEvent(event)
    }

    override fun getScrollDistance(): Int {
        return scrollY
    }

    override fun isCanScroll(): Boolean {
        return true
    }
}