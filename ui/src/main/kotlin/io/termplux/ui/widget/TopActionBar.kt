package io.termplux.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import io.termplux.ui.R
import io.termplux.ui.preview.WidgetPreview

@Composable
fun TopActionBar(
    factory: MaterialToolbar,
    modifier: Modifier,
) {
    val toolbarParams: AppBarLayout.LayoutParams = AppBarLayout.LayoutParams(
        AppBarLayout.LayoutParams.MATCH_PARENT,
        AppBarLayout.LayoutParams.WRAP_CONTENT
    )

    AndroidView(
        factory = { context ->
            AppBarLayout(context).apply {
                addView(factory, toolbarParams)
            }
        },
        onReset = { appBar ->
            appBar.removeView(factory)
            appBar.addView(factory, toolbarParams)
        },
        modifier = modifier,
        update = { appBar ->
            appBar.setBackgroundColor(Color.Transparent.value.toInt())
            appBar.isLiftOnScroll = true
            appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                appBar.context
            )
        },
        onRelease = { appBar ->
            appBar.removeView(factory)
        }
    )
}

@Composable
@WidgetPreview
fun TopActionBarPreview() {
    TopActionBar(
        factory = MaterialToolbar(LocalContext.current).apply {
            title = "topappbar"
            navigationIcon = ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.baseline_arrow_back_24
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}