package io.termplux.ui.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.ui.layout.ActivityMain
import io.termplux.ui.theme.TermPluxAppTheme

class UI private constructor() : UIWrapper {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun content(context: Context, activity: Activity, container: View): View {
        return withView(context) {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = activity)
            val displayFeatures: List<DisplayFeature> = calculateDisplayFeatures(activity = activity)
            TermPluxAppTheme {
               ActivityMain(
                   windowSize = windowSize,
                   displayFeatures = displayFeatures,
                   container = container as FragmentContainerView,
                   flutter = FrameLayout(context),
                   appsUpdate = {},
                   topBarVisible = true,
                   topBarView = MaterialToolbar(context),
                   preferenceUpdate = {},
                   androidVersion = "",
                   shizukuVersion = "",
                   current = {},
                   toggle = { /*TODO*/ },
                   taskbar = {}
               )
            }
        }
    }

    private fun withView(
        context: Context,
        content: @Composable () -> Unit,
    ): ComposeView {
        return ComposeView(
            context = context
        ).apply {
            setContent(
                content = content
            )
        }
    }

    companion object {

        fun build(): UI {
            return UI()
        }
    }
}