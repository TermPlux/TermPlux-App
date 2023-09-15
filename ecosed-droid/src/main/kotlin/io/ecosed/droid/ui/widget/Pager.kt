package io.ecosed.droid.ui.widget

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2
import io.ecosed.droid.ui.preview.WidgetPreview
import io.ecosed.droid.ui.theme.LibEcosedTheme

@Composable
fun Pager(
    modifier: Modifier,
    viewPager2: ViewPager2
) {
    val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    )

    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                addView(viewPager2, params)
            }
        },
        onReset = { frame ->
            frame.removeView(viewPager2)
            frame.addView(viewPager2, params)
        },
        modifier = modifier,
        onRelease = { frame ->
            frame.removeView(viewPager2)
        }
    )
}

@Composable
@WidgetPreview
fun HomePagerPreview() {
    LibEcosedTheme {
        Pager(
            modifier = Modifier.fillMaxSize(),
            viewPager2 = ViewPager2(LocalContext.current)
        )
    }
}