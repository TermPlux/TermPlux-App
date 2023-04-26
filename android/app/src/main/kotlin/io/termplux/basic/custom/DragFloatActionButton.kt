package com.learnplus.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.sqrt

class DragFloatActionButton : FloatingActionButton {

    private var parentHeight = 0
    private var parentWidth = 0

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

    private var lastX = 0
    private var lastY = 0
    private var isDrag = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(true)
                lastX = rawX
                lastY = rawY
                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    parentHeight = parent.height
                    parentWidth = parent.width
                }
            }

            MotionEvent.ACTION_MOVE -> {
                isDrag = !(parentHeight <= 0 || parentWidth == 0)
                val dx = rawX - lastX
                val dy = rawY - lastY
                val distance = sqrt((dx * dx + dy * dy).toDouble()).toInt()
                if (distance == 0) {
                    isDrag = false
                }
                var x = x + dx
                var y = y + dy
                x = if (x < 0) 0F else (if (x > parentWidth - width) parentWidth - width else x).toFloat()
                y = if (y < 0) 0F else (if (y + height > parentHeight) parentHeight - height else y).toFloat()
                setX(x)
                setY(y)
                lastX = rawX
                lastY = rawY
            }

            MotionEvent.ACTION_UP -> if (!isNotDrag()) {
                isPressed = false
                if (rawX >= parentWidth / 2) {
                    animate().setInterpolator(DecelerateInterpolator())
                        .setDuration(500)
                        .xBy(parentWidth - width - x)
                        .start()
                } else {
                    val oa = ObjectAnimator.ofFloat(this, "x", x, 0f)
                    oa.interpolator = DecelerateInterpolator()
                    oa.duration = 500
                    oa.start()
                }
            }
        }
        return !isNotDrag() || super.onTouchEvent(event)
    }

    private fun isNotDrag(): Boolean{
        return !isDrag && (x.toInt() == 0 || x.toInt() == parentWidth - width)
    }
}