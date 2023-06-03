package io.termplux.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import io.termplux.R
import io.termplux.holder.PagerViewHolder

class PagerAdapter constructor(
    rootLayout: FrameLayout,
    composeView: ComposeView,
    refreshLayout: SwipeRefreshLayout,
    viewPager: ViewPager2
) : RecyclerView.Adapter<PagerViewHolder>() {

    private val mRootLayout: FrameLayout
    private val mComposeView: ComposeView
    private val mRefreshLayout: SwipeRefreshLayout
    private val mViewPager: ViewPager2
    private lateinit var mTextView: AppCompatTextView

    init {
        mRootLayout = rootLayout
        mComposeView = composeView
        mRefreshLayout = refreshLayout
        mViewPager = viewPager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(
            itemView = FrameLayout(
                parent.context
            ).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        ).also {
            mTextView = AppCompatTextView(parent.context).apply {
                text = parent.context.getString(R.string.error)
                textSize = 50f
                gravity = Gravity.CENTER
                setTextColor(Color.RED)
            }
        }
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        (holder.itemView as FrameLayout).addView(
            when (position) {
                root -> mRootLayout
                compose -> mComposeView
                refresh -> mRefreshLayout
                settings -> mViewPager
                else -> mTextView
            }.also {
                if (position != refresh) it.apply {
                    background = ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.color.pager_background
                    )
                }
            },
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    companion object {
        const val root: Int = 0
        const val compose: Int = 1
        const val refresh: Int = 2
        const val settings: Int = 3

        val count: Int = arrayOf(root, compose, refresh, settings).size
    }
}