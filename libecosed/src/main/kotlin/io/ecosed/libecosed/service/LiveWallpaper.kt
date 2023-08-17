package io.ecosed.libecosed.service

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ScreenUtils
import io.ecosed.libecosed.R

internal class LiveWallpaper : WallpaperService() {

    // 背景
    private lateinit var background: Drawable

    override fun onCreateEngine(): Engine = object : Engine() {

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            // 获取背景资源
            background = ContextCompat.getDrawable(
                this@LiveWallpaper,
                R.drawable.custom_wallpaper_24
            )!!
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            val canvas: Canvas = holder.lockCanvas()
            // 获取手机屏幕长宽作为位图尺寸
            val width = ScreenUtils.getScreenWidth()
            val height = ScreenUtils.getScreenHeight()
            // 设置背景参数
            background.setBounds(0, 0, width, height)
            // 将背景绘制在画布上
            background.draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
        }
    }
}