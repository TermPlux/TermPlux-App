package io.termplux.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import com.kongzue.dialogx.interfaces.ScrollController

class ScrollControllerWebView : WebView, ScrollController {

    constructor(
        context: Context
    ) : super(
        context
    )

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context,
        attrs
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

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