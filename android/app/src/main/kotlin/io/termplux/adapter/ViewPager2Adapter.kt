package io.termplux.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import io.termplux.R


class ViewPager2Adapter constructor(
    flutterView: View,
    composeView: ComposeView
) : RecyclerView.Adapter<ViewPagerViewHolder>() {

    private val mFlutterView: View
    private val mComposeView: ComposeView
    private lateinit var mTextView: AppCompatTextView

    init {
        mFlutterView = flutterView
        mComposeView = composeView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        mTextView = AppCompatTextView(parent.context).apply {
            text = parent.context.getString(R.string.error)
            textSize = 50f
            setTextColor(Color.RED)
        }
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