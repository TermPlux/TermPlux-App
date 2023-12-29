package io.termplux.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log

class WallpaperUtil private constructor(){

    private fun getWallpaper(context: Context): Drawable? {
        return try {
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.getBuiltInDrawable(WallpaperManager.FLAG_SYSTEM)
        } catch (e: IllegalArgumentException) {
            Log.e("wallpaper", "获取壁纸失败")
            null
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        fun drawable(context: Context): Drawable? {
            return WallpaperUtil().getWallpaper(context)
        }
    }
}