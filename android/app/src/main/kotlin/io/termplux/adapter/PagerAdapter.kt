package io.termplux.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.termplux.R
import io.termplux.holder.PagerViewHolder

class PagerAdapter constructor(
    flutterView: View,
    composeView: View,
    recyclerView: View,
) : RecyclerView.Adapter<PagerViewHolder>() {

    private val mFlutterView: View
    private val mComposeView: View
    private val mRecyclerView: View
    private lateinit var mTextView: AppCompatTextView

    init {
        mFlutterView = flutterView
        mComposeView = composeView
        mRecyclerView = recyclerView
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
        return arrayOf(flutter, compose, recycler).size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        (holder.itemView as FrameLayout).apply {
            if (position != recycler) background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.color.pager_background
            )
            addView(
                when (position) {
                    flutter -> mFlutterView
                    compose -> mComposeView
                    recycler -> mRecyclerView
                    else -> mTextView
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    companion object {
        const val flutter: Int = 0
        const val compose: Int = 1
        const val recycler: Int = 2
    }
}