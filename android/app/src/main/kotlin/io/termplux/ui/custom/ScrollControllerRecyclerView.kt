package io.termplux.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialogx.interfaces.ScrollController

class ScrollControllerRecyclerView(
    context: Context
) : RecyclerView(
    context
), ScrollController {

    private var mLockScroll = false

    override fun getScrollDistance(): Int {
        return scrollY
    }

    override fun isCanScroll(): Boolean {
        return true
    }

    override fun lockScroll(lockScroll: Boolean) {
        mLockScroll = lockScroll
    }

    override fun isLockScroll(): Boolean {
        return mLockScroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        return if (mLockScroll) false else super.onTouchEvent(event)
    }
}