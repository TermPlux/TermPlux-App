package io.termplux.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.termplux.ui.preview.TermPluxPreviews

@Composable
fun ScreenPreference(
    preference: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        preference(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@TermPluxPreviews
fun ScreenPreferencePreview() {
    ScreenPreference { modifier ->
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Preference Preview",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}