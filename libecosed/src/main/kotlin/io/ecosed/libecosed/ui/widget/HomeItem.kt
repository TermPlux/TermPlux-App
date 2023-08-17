package io.ecosed.libecosed.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ecosed.libecosed.ui.preview.WidgetPreview
import io.ecosed.libecosed.ui.theme.LibEcosedTheme

@Composable
fun HomeItem(title: String, body: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(
        modifier = Modifier.height(
            height = 24.dp
        )
    )
}

@Composable
@WidgetPreview
fun HomeItemPreview() {
    LibEcosedTheme {
        HomeItem(
            title = "666",
            body = "666"
        )
    }
}