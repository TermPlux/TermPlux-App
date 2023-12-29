package io.termplux.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import io.termplux.utils.WallpaperUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        window.decorView.background = WallpaperUtil.drawable(this)

        setContentView(R.layout.activity_main)
    }
}