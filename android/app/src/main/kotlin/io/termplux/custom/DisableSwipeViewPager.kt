package io.termplux.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import io.termplux.R

class DisableSwipeViewPager : ViewPager {

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

    private var canSwipe = false

    init {
        background = ContextCompat.getDrawable(
            context,
            R.drawable.custom_wallpaper_24
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return canSwipe && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return canSwipe && super.onInterceptTouchEvent(ev)
    }
}