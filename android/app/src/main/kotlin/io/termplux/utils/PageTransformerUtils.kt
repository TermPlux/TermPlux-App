package io.termplux.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import io.termplux.R
import kotlin.math.abs

class PageTransformerUtils : PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> {
                    // 禁用圆角动画
                    clipToOutline = false
                    // 不透明
                    alpha = 0f
                }

                position <= 1 -> {
                    val scaleFactor = minScale.coerceAtLeast(1 - abs(position))
                    val verticalMargin = height * (1 - scaleFactor) / 2
                    val horizontalMargin = width * (1 - scaleFactor) / 2
                    // 获取圆角大小
                    val round = context.resources.getDimension(R.dimen.pager_round).toInt()
                    // 深景缩放动画
                    translationX = if (position < 0) {
                        horizontalMargin - verticalMargin / 2
                    } else {
                        horizontalMargin + verticalMargin / 2
                    }
                    // 滑动监听
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    // 启用圆角动画
                    clipToOutline = true
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View, outline: Outline) {
                            outline.setRoundRect(
                                0,
                                0,
                                view.width,
                                view.height,
                                (round + (((scaleFactor - minScale) / (1 - minScale)) * (1 - round)))
                            )
                        }
                    }
                    // 透明度
                    alpha =
                        (minAlpha + (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                }

                else -> {
                    // 禁用圆角动画
                    clipToOutline = false
                    // 不透明
                    alpha = 0f
                }
            }
        }
    }

    companion object {
        const val minScale = 0.85f
        const val minAlpha = 0.8f
    }
}