package io.termplux.utils

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import io.termplux.values.Codes
import io.termplux.values.Key

class MainActivityUtils(
    private val mContext: Context
) {



    /**
     * Drawable转Bitmap
     */

    @SuppressLint("MissingPermission")
    fun bitmapWallpaper(): Bitmap {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(mContext) //获取壁纸管理器实例
        val bitmap: Bitmap //声明将要创建的bitmap
        val width = wallpaperManager.drawable.intrinsicWidth //获取图片宽度
        val height = wallpaperManager.drawable.intrinsicHeight //获取图片高度
        val config = Bitmap.Config.RGB_565 //图片位深
        bitmap = Bitmap.createBitmap(width, height, config) //创建一个空的Bitmap
        val canvas = Canvas(bitmap) //在bitmap上创建一个画布
        wallpaperManager.drawable.setBounds(0, 0, width, height) //设置画布的范围
        wallpaperManager.drawable.draw(canvas) //将drawable绘制在canvas上
        return bitmap
    }

    companion object {
        private val code = Codes
        private val key = Key

        const val none: Int = code.modeNone
        const val shizuku: Int = code.modeShizuku

        const val licence: String = key.licence
        const val dynamicColor: String = key.dynamicColor
        const val libTaskbar: String = key.libTaskBar

        /** 开屏图标动画时长 */
        const val splashAnimatorMillis = 600

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300
    }
}