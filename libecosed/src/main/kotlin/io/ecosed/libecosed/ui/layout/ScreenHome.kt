package io.ecosed.libecosed.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.viewpager2.widget.ViewPager2
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.ui.widget.Pager

@Composable
internal fun ScreenHome(
    viewPager2: ViewPager2
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(
                    id = R.drawable.custom_wallpaper_24
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Pager(
                modifier = Modifier.fillMaxSize(),
                viewPager2 = viewPager2
            )
        }

    }
}

@ScreenPreviews
@Composable
private fun ScreenHomePreview() {
    LibEcosedTheme {
        ScreenHome(viewPager2 = ViewPager2(LocalContext.current))
    }
}