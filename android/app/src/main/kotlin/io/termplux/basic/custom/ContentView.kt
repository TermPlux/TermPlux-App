package io.termplux.basic.custom

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable

@SuppressLint("ViewConstructor")
class ContentView constructor(
    context: Context,
    toolbar: MaterialToolbar,
    composeView: ComposeView,
    viewPager: ViewPager2,
    isFull: Boolean
) : LinearLayoutCompat(
    context
) {

    /** 上下文 */
    private val mContext: Context

    private val mToolBar: MaterialToolbar

    /** ComposeView */
    private val mComposeView: ComposeView

    /** ViewPager2 */
    private val mViewPager2: ViewPager2

    /** 是否为精简模式 */
    private val mFull: Boolean

    init {
        // 初始化
        mContext = context

        mToolBar = toolbar
        mComposeView = composeView
        mViewPager2 = viewPager
        mFull = isFull

        addParams()
        addView()
    }

    private fun addParams() {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        orientation = VERTICAL
    }

    private fun addView() {
        addView(
            AppBarLayout(
                mContext
            ).apply {
                fitsSystemWindows = true
                isLiftOnScroll = true
                statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                    mContext
                )
                addView(
                    mToolBar.apply {
                        fitsSystemWindows = true
                    },
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    )
                )
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addView(
            if (mFull) mComposeView.apply {
                setViewCompositionStrategy(
                    strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
            } else mViewPager2.apply {
                fitsSystemWindows = true
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
//        addView(
//            CoordinatorLayout(
//                mContext
//            ).apply {
//
//            },
//            LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT
//            )
//        )

    }
}