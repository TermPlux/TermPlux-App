package io.termplux.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2

@Composable
fun ScreenFlutter(viewPager: ViewPager2) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(
            factory = { viewPager },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun ScreenFlutterPreview() {
    ScreenFlutter(
        viewPager = ViewPager2(
            LocalContext.current
        )
    )
}