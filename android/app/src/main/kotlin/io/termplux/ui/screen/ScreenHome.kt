package io.termplux.ui.screen

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.termplux.R
import io.termplux.ui.preview.TermPluxPreviews

@Composable
fun ScreenHome(
    rootLayout: FrameLayout
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(
            factory = {
                return@AndroidView rootLayout
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@TermPluxPreviews
fun ScreenHomePreview() {
    val context = LocalContext.current
    ScreenHome(
        rootLayout = FrameLayout(context).apply {
            addView(
                TextView(context).apply {
                    text = stringResource(
                        id = R.string.flutter_view_preview
                    )
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    )
}