package io.termplux.basic.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.BarUtils

@SuppressLint("ViewConstructor")
class FragmentScaffold constructor(
    context: Context,
    view: View
) : FrameLayout(
    context
) {

    init {
        setPadding(
            0,
            statusBarHeight(context = context),
            0,
            navigationBarHeight(context = context)
        )
        addView(
            view,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
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