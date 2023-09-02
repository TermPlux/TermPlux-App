package io.ecosed.libecosed.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.ui.widget.Pager
import io.ecosed.libecosed.ui.widget.TopActionBar

@Composable
internal fun ScreenHome(
    navController: NavHostController,
    topBarVisible: Boolean,
    drawerState: DrawerState,
    topBarUpdate: (MaterialToolbar) -> Unit,
    viewPager2: ViewPager2
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Pager(
            modifier = Modifier.fillMaxSize(),
            viewPager2 = viewPager2
        )
//        Column(modifier = Modifier.fillMaxSize()) {
//            TopActionBar(
//                navController = navController,
//                modifier = Modifier.fillMaxWidth(),
//                visible = topBarVisible,
//                drawerState = drawerState,
//                update = topBarUpdate
//            )
//            Pager(
//                modifier = Modifier.fillMaxSize(),
//                viewPager2 = viewPager2
//            )
//        }
//        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painterResource(
//                    id = R.drawable.custom_wallpaper_24
//                ),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.FillBounds
//            )
//            Pager(
//                modifier = Modifier.fillMaxSize(),
//                viewPager2 = viewPager2
//            )
//        }

    }
}

@ScreenPreviews
@Composable
private fun ScreenHomePreview() {
    LibEcosedTheme {
        ScreenHome(
            navController = rememberNavController(),
            topBarVisible = true,
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            ),
            topBarUpdate = {},
            viewPager2 = ViewPager2(LocalContext.current)
        )
    }
}