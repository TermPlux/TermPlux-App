package io.termplux.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import io.termplux.R
import io.termplux.ui.preview.WidgetPreview

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun TopActionBar(
    modifier: Modifier,
    visible: Boolean,
    update: (MaterialToolbar) -> Unit
) {
    val toolbar: MaterialToolbar = MaterialToolbar(
        LocalContext.current
    ).apply {
        setBackgroundColor(Color.Transparent.value.toInt())
    }.also { toolbar ->
        update(toolbar)
    }

    val toolbarParams: AppBarLayout.LayoutParams = AppBarLayout.LayoutParams(
        AppBarLayout.LayoutParams.MATCH_PARENT,
        AppBarLayout.LayoutParams.WRAP_CONTENT
    )

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        label = ""
    ) {
        AndroidView(
            factory = { context ->
                AppBarLayout(context).apply {
                    addView(toolbar, toolbarParams)
                }
            },
            onReset = { appBar ->
                appBar.removeView(toolbar)
                appBar.addView(toolbar, toolbarParams)
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
                appBar.removeView(toolbar)
            }
        )
    }
}

@Composable
@WidgetPreview
fun TopActionBarPreview() {
    TopActionBar(
        modifier = Modifier.fillMaxWidth(),
        visible = true
    ) {
        it.apply {
            title = context.getString(
                R.string.toolbar_preview
            )
            navigationIcon = ContextCompat.getDrawable(
                context,
                R.drawable.baseline_arrow_back_24
            )
        }
    }
}