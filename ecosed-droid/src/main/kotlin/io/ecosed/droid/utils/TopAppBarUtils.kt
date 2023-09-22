package io.ecosed.droid.utils

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal class TopAppBarUtils private constructor() {

    private var mVisible: Boolean by mutableStateOf(value = true)
    private var topAppBarVisible: Boolean by mutableStateOf(value = true)

    private val mHandler = Looper.myLooper()?.let {
        Handler(it)
    }

    private val showPart2Runnable = Runnable {
        topAppBarVisible = true
    }

    private val hideRunnable = Runnable {
        hide()
    }

    private val delayHideTouchListener: View.OnTouchListener =
        View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> if (autoHide) delayedHide()
                MotionEvent.ACTION_UP -> view.performClick()
            }
            false
        }

    internal fun attach(attach: (Boolean, View.OnTouchListener, () -> Unit) -> Unit): TopAppBarUtils {
        mVisible = true
        attach(topAppBarVisible, delayHideTouchListener) {
            toggle()
        }
        return this@TopAppBarUtils
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        topAppBarVisible = false
        mVisible = false
        mHandler?.removeCallbacks(showPart2Runnable)
    }

    private fun show() {
        mVisible = true
        mHandler?.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
    }

    private fun delayedHide() {
        mHandler?.removeCallbacks(hideRunnable)
        mHandler?.postDelayed(hideRunnable, autoHideDelayMillis.toLong())
    }

    internal companion object {

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        private const val autoHide = false

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        private const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        private const val uiAnimatorDelay = 300

        internal fun newInstance(): TopAppBarUtils {
            return TopAppBarUtils()
        }
    }
}