package io.termplux.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.viewpager2.widget.ViewPager2
import io.flutter.embedding.android.FlutterFragment
import io.termplux.adapter.MainAdapter
import io.termplux.ui.navigation.Screen

@Composable
fun ScreenHome(
    navController: NavHostController,
    viewPager: ViewPager2,
    activity: FragmentActivity,
    flutter: FlutterFragment,
    viewPagerAdapter: (FragmentActivity, FlutterFragment, ViewPager2, () -> Unit) -> MainAdapter,
) {
    val mainAdapter: MainAdapter = viewPagerAdapter(
        activity,
        flutter,
        viewPager
    ) {
        navController.navigate(
            route = Screen.Settings.route
        ) {
            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(
            factory = {
                viewPager.apply {
                    adapter = mainAdapter
                    //isUserInputEnabled = false
                    offscreenPageLimit = mainAdapter.itemCount
                    //currentItem = 1
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun ScreenHomePreview() {
    ScreenHome(
        navController = rememberNavController(),
        viewPager = ViewPager2(
            LocalContext.current
        ),
        activity = FragmentActivity(),
        flutter = FlutterFragment(),
        viewPagerAdapter = { activity, flutter, viewPager, settings ->
            MainAdapter(
                activity = activity,
                flutter = flutter,
                viewPager = viewPager,
                settings = settings
            )
        }
    )
}