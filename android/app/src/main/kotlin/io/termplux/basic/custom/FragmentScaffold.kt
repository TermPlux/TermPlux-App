package io.termplux.basic.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.appbar.AppBarLayout

class FragmentScaffold : FrameLayout {

    constructor(
        context: Context,
    ) : super(
        context
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
//        setPadding(
//            0,
//            statusBarHeight(context = context),
//            0,
//            navigationBarHeight(context = context)
//        )
    }

    private fun statusBarHeight(context: Context): Int {
        return if (
            BarUtils.isStatusBarVisible(
                context as Activity
            )
        ) {
            BarUtils.getStatusBarHeight()
        } else {
            0
        }
    }

    private fun navigationBarHeight(context: Context): Int {
        return if (
            BarUtils.isNavBarVisible(
                context as Activity
            )
        ) {
            BarUtils.getNavBarHeight()
        } else {
            0
        }
    }
}