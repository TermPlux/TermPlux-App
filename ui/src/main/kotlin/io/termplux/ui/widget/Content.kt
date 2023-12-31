package io.termplux.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import io.termplux.ui.preview.WidgetPreview
import io.termplux.ui.theme.TermPluxAppTheme

@Composable
fun Content(
    container: FragmentContainerView,
    modifier: Modifier
) {
    container.apply {
        AndroidView(
            factory = {
                return@AndroidView this@apply
            },
            modifier = modifier
        )
    }
}

@Composable
@WidgetPreview
fun ContentPreview() {
    TermPluxAppTheme {
        Content(
            container = FragmentContainerView(LocalContext.current),
            modifier = Modifier.fillMaxSize()
        )
    }
}