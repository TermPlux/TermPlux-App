package io.termplux.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.termplux.ui.preview.ScreenPreviews

@Composable
fun ScreenApps(
    appsGrid: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        appsGrid(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@ScreenPreviews
@Composable
fun ScreenAppsPreview() {
    ScreenApps {

    }
}