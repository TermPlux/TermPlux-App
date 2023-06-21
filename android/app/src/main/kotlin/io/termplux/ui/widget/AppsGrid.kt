package io.termplux.ui.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.termplux.ui.preview.WidgetPreview

@Composable
fun AppsGrid(
    modifier: Modifier,
    update: (RecyclerView) -> Unit
) {
    AndroidView(
        factory = { context ->
            RecyclerView(context)
        },
        modifier = modifier,
        update = { apps ->
            apps.layoutManager = GridLayoutManager(
                apps.context,
                4,
                RecyclerView.VERTICAL,
                false
            )
            update(apps)
        }
    )
}

@Composable
@WidgetPreview
fun AppsGridPreview() {
    AppsGrid(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}