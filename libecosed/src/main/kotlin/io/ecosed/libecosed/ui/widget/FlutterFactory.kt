package io.ecosed.libecosed.ui.widget

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.WidgetPreview
import io.ecosed.libecosed.ui.theme.LibEcosedTheme

@Composable
fun FlutterFactory(
    factory: FrameLayout,
    modifier: Modifier
) {
    AndroidView(
        factory = {
            factory
        },
        modifier = modifier
    )
}

@Composable
@WidgetPreview
fun FlutterFactoryPreview() {
    LibEcosedTheme {
        FlutterFactory(
            factory = FrameLayout(LocalContext.current).apply {
                addView(
                    TextView(LocalContext.current).apply {
                        text = stringResource(
                            id = R.string.flutter_view_preview
                        )
                        gravity = Gravity.CENTER
                    },
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}