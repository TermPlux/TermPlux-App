package io.ecosed.libecosed.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import io.ecosed.libecosed.ui.preview.WidgetPreview
import io.ecosed.libecosed.ui.theme.LibEcosedTheme

@Composable
fun NavHostFactory(
    factory: FragmentContainerView,
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
fun NavHostFactoryPreview() {
    LibEcosedTheme {
        NavHostFactory(
            factory = FragmentContainerView(
                context = LocalContext.current
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}