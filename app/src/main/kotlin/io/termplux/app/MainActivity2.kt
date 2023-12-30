package io.termplux.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import io.termplux.base.TPBaseActivity
import io.termplux.utils.WallpaperUtil

class MainActivity2 : TPBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        window.decorView.background = WallpaperUtil.drawable(this)

       // setContentView(R.layout.activity_main)

        flutter
    }
}