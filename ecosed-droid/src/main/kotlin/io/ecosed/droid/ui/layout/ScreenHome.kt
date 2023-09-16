package io.ecosed.droid.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.droid.ui.preview.ScreenPreviews
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.ui.widget.Pager

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
        OutlinedCard(
            modifier = Modifier.fillMaxSize().padding(all = 12.dp)
        ) {
            Pager(
                modifier = Modifier.fillMaxSize(),
                viewPager2 = viewPager2
            )
        }

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