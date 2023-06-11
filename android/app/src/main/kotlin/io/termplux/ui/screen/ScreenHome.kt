package io.termplux.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.termplux.R
import io.termplux.ui.preview.TermPluxPreviews

@Composable
fun ScreenHome(
    rootLayout: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        rootLayout(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
@TermPluxPreviews
fun ScreenHomePreview() {
    ScreenHome { modifier ->
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(
                    id = R.string.view_pager_preview
                ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}