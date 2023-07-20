package io.termplux.app.activity

import android.widget.FrameLayout
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import io.termplux.framework.termplux.ComposeActivity
import io.termplux.ui.layout.ActivityMain
import io.termplux.ui.theme.TermPluxTheme

class MainActivity : ComposeActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Contents() {
        super.Contents()
        val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this@MainActivity)
        val displayFeatures: List<DisplayFeature> = calculateDisplayFeatures(activity = this@MainActivity)

        TermPluxTheme {
            ActivityMain(
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                rootLayout = FrameLayout(this@MainActivity),
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