package io.termplux.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.FlutterDash
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Navigation
import androidx.compose.material.icons.twotone.RocketLaunch
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Terminal
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.R
import io.termplux.basic.adapter.MainAdapter

sealed class Screen constructor(
    val type: ScreenType,
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int,
) {

    object Home : Screen(
        type = ScreenType.Compose,
        route = ScreenRoute.routeHome,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_home
    )

    object Dashboard : Screen(
        type = ScreenType.Compose,
        route = ScreenRoute.routeDashboard,
        imageVector = Icons.TwoTone.Dashboard,
        title = R.string.menu_dashboard
    )

    object Content : Screen(
        type = ScreenType.Compose,
        route = ScreenRoute.routeContent,
        imageVector = Icons.TwoTone.Terminal,
        title = R.string.menu_content
    )

    object Settings : Screen(
        type = ScreenType.Compose,
        route = ScreenRoute.routeSettings,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )

    object About : Screen(
        type = ScreenType.Compose,
        route = ScreenRoute.routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )


    object LauncherFragment : Screen(
        type = ScreenType.Fragment,
        route = MainAdapter.launcher.toString(),
        imageVector = Icons.TwoTone.RocketLaunch,
        title = R.string.menu_launcher
    )

    object HomeFragment : Screen(
        type = ScreenType.Fragment,
        route = MainAdapter.home.toString(),
        imageVector = Icons.TwoTone.FlutterDash,
        title = R.string.menu_home
    )

    object NavigationFragment : Screen(
        type = ScreenType.Fragment,
        route = MainAdapter.nav.toString(),
        imageVector = Icons.TwoTone.Navigation,
        title = R.string.menu_navigation
    )

    object SettingsFragment : Screen(
        type = ScreenType.Fragment,
        route = MainAdapter.settings.toString(),
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )
}