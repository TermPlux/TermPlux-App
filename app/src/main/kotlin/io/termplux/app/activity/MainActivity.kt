package io.termplux.app.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import io.termplux.ui.layout.ActivityMain
import io.termplux.ui.theme.TermPluxTheme
import rikka.material.app.MaterialActivity

class MainActivity : MaterialActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
            val displayFeatures: List<DisplayFeature> = calculateDisplayFeatures(activity = this@MainActivity)

            TermPluxTheme {
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    rootLayout = FrameLayout(this),
                    appsUpdate = {},
                    topBarVisible = true,
                    topBarUpdate = {},
                    preferenceUpdate = {},
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = {},
                    toggle = {},
                    taskbar = {}
                )
            }
        }
    }
}