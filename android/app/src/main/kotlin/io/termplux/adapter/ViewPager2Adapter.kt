package io.termplux.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView


class ViewPager2Adapter constructor(
    flutterView: View,
    composeView: ComposeView
) : RecyclerView.Adapter<ViewPagerViewHolder>() {

    private val mFlutterView: View
    private val mComposeView: ComposeView

    init {
        mFlutterView = flutterView
        mComposeView = composeView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            itemView = FrameLayout(
                parent.context
            ).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        (holder.itemView as FrameLayout).apply {
            addView(
                when (position) {
                    0 -> mFlutterView
                    1 -> mComposeView
                    else -> View(context)
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }
}