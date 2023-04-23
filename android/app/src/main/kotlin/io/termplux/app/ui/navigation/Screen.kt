package io.termplux.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Apps
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

sealed class Screen constructor(
    val type: ScreenType,
    val item: ItemType,
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int,
) {

    object ComposeTitle : Screen(
        type = ScreenType.Title,
        item = ItemType.Title,
        route = ScreenRoute.Title,
        imageVector = Icons.Filled.Android,
        title = R.string.title_compose
    )

    object Home : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeHome,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_home
    )

    object Dashboard : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeDashboard,
        imageVector = Icons.TwoTone.Dashboard,
        title = R.string.menu_dashboard
    )

    object Manager : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeContent,
        imageVector = Icons.TwoTone.AppSettingsAlt,
        title = R.string.menu_manager
    )

    object Settings : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeSettings,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )

    object About : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )

    object Divider : Screen(
        type = ScreenType.Divider,
        item = ItemType.Divider,
        route = ScreenRoute.Divider,
        imageVector = Icons.Filled.Android,
        title = R.string.app_name
    )

    object FragmentTitle : Screen(
        type = ScreenType.Title,
        item = ItemType.Title,
        route = ScreenRoute.Title,
        imageVector = Icons.Filled.Android,
        title = R.string.title_fragment
    )


    object LauncherFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeLauncherFragment,
        imageVector = Icons.TwoTone.RocketLaunch,
        title = R.string.menu_launcher
    )

    object HomeFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeHomeFragment,
        imageVector = Icons.TwoTone.FlutterDash,
        title = R.string.menu_home
    )

    object AppsFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeAppsFragment,
        imageVector = Icons.TwoTone.Apps,
        title = R.string.menu_apps
    )

    object NavigationFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeNavigationFragment,
        imageVector = Icons.TwoTone.Navigation,
        title = R.string.menu_navigation
    )

    object SettingsFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeSettingsFragment,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )
}