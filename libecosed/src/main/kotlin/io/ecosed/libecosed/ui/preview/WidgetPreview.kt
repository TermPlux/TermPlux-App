package io.ecosed.libecosed.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(
    name = "Widget",
    group = "Ecosed Framework",
    apiLevel = 33,
    locale = "zh-rCN",
    fontScale = 1.0f,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED,
    showSystemUi = false,
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
internal annotation class WidgetPreview