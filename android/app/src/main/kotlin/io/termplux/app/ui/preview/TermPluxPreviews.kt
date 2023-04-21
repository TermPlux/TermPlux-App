package io.termplux.app.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "TermPlux",
    group = "TermPlux",
    device = "id:pixel_6_pro",
    apiLevel = 33,
    locale = "zh-rCN",
    fontScale = 1.0f,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_UNDEFINED
)
annotation class TermPluxPreviews