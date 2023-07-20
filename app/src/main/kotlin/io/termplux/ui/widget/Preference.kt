package io.termplux.ui.widget

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2
import io.termplux.ui.preview.WidgetPreview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Preference(
    modifier: Modifier,
    update: (ViewPager2) -> Unit
) {
    val preference = ViewPager2(
        LocalContext.current
    ).apply {
        isUserInputEnabled = true
    }.also {
        update(it)
    }

    val preferenceParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    )

    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                addView(preference, preferenceParams)
            }
        },
        onReset = { frame ->
            frame.removeView(preference)
            frame.addView(preference, preferenceParams)
        },
        modifier = modifier,
        onRelease = { frame ->
            frame.removeView(preference)
        }
    )
}

@Composable
@WidgetPreview
fun PreferencePreview() {

}