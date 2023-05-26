package io.termplux.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import io.termplux.R

class ViewPager2Adapter constructor(
    flutterView: View,
    composeView: ComposeView,
    recyclerView: RecyclerView,
    fragmentContainerView: FragmentContainerView
) : RecyclerView.Adapter<ViewPagerViewHolder>() {

    private val mFlutterView: View
    private val mComposeView: ComposeView
    private val mRecyclerView: RecyclerView
    private val mFragmentContainerView: FragmentContainerView
    private lateinit var mTextView: AppCompatTextView

    init {
        mFlutterView = flutterView
        mComposeView = composeView
        mRecyclerView = recyclerView
        mFragmentContainerView = fragmentContainerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            itemView = FrameLayout(
                parent.context
            ).apply {
                background = ContextCompat.getDrawable(
                    parent.context,
                    R.color.pager_background
                )
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
        return 4
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        (holder.itemView as FrameLayout).apply {
            addView(
                when (position) {
                    0 -> mFlutterView
                    1 -> mComposeView
                    2 -> mRecyclerView
                    3 -> mFragmentContainerView
                    else -> mTextView
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }
}