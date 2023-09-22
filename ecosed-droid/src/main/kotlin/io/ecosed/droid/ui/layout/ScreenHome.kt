package io.ecosed.droid.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ecosed.droid.ui.preview.ScreenPreviews
import io.ecosed.droid.ui.theme.LibEcosedTheme

@Composable
internal fun ScreenHome(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    all = 12.dp
                )
        ) {
            content()
        }
    }
}

@ScreenPreviews
@Composable
private fun ScreenHomePreview() {
    LibEcosedTheme {
        ScreenHome {

        }
    }
}