package io.termplux.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.termplux.R
import io.termplux.app.ui.preview.TermPluxPreviews

@Composable
fun ScreenHome(
    pager: @Composable (modifier: Modifier) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        pager(
            modifier = Modifier.fillMaxSize()
        )
    }
//    Scaffold(
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(
//                    paddingValues = innerPadding
//                ),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            pager(
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
}

@Composable
@TermPluxPreviews
fun ScreenHomePreview() {
    ScreenHome(
        pager = { modifier ->
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
    )
}