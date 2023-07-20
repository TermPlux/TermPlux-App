package io.termplux.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Apps
import androidx.compose.material.icons.twotone.FlutterDash
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.ListAlt
import androidx.compose.material.icons.twotone.RoomPreferences
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.R
import io.termplux.ui.navigation.ItemType
import io.termplux.ui.navigation.ScreenRoute
import io.termplux.ui.navigation.ScreenType

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
        title = R.string.menu_apps
    )

    object Apps: Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = "apps",
        imageVector = Icons.TwoTone.Apps,
        title = R.string.menu_apps
    )

    object Flutter : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeHome,
        imageVector = Icons.TwoTone.FlutterDash,
        title = R.string.menu_flutter
    )

    object Overview: Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeDashboard,
        imageVector = Icons.TwoTone.ListAlt,
        title = R.string.menu_overview
    )

//    object Dashboard : Screen(
//        type = ScreenType.Compose,
//        item = ItemType.Default,
//        route = ScreenRoute.routeDashboard,
//        imageVector = Icons.TwoTone.List,
//        title = R.string.menu_overview
//    )

    object Manager : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeManager,
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

    object Preference : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = "preference",
        imageVector = Icons.TwoTone.RoomPreferences,
        title = R.string.menu_preference
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
        title = R.string.app_name
    )

    object LauncherFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeLauncherFragment,
        imageVector = Icons.Filled.RocketLaunch,
        title = R.string.app_name
    )

    object HomeFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeHomeFragment,
        imageVector = Icons.Filled.FlutterDash,
        title = R.string.app_name
    )

    object AppsFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeAppsFragment,
        imageVector = Icons.Filled.Apps,
        title = R.string.menu_apps
    )

    object SettingsFragment : Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = ScreenRoute.routeSettingsFragment,
        imageVector = Icons.Filled.Settings,
        title = R.string.menu_settings
    )
}