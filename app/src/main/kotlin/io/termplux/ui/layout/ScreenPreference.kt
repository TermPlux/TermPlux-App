package io.termplux.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.viewpager2.widget.ViewPager2
import io.termplux.ui.preview.ScreenPreviews
import io.termplux.ui.widget.Preference

@Composable
fun ScreenPreference(
    preferenceUpdate: (ViewPager2) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Preference(
            modifier = Modifier.fillMaxSize(),
            update = preferenceUpdate
        )
    }
}

@Composable
@ScreenPreviews
fun ScreenPreferencePreview() {
    ScreenPreference { modifier ->
//        Box(
//            modifier = modifier,
//            contentAlignment = Alignment.Center,
//        ) {
//            Text(
//                text = "Preference Preview",
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.titleLarge
//            )
//        }
    }
}