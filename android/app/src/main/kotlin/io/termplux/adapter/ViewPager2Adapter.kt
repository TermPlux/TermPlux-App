package io.termplux.adapter

import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView


class ViewPager2Adapter constructor(
    flutterView: View,
    composeView: ComposeView
) : RecyclerView.Adapter<ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val itemView: View = View(parent.context)

        return ViewPagerViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        when (position) {
            0 -> {
                //隐藏Compose显示Flutter
            }

            1 -> {
                // 隐藏Flutter显示Compose
            }
        }
    }
}