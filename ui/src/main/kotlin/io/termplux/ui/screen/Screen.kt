package io.termplux.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Apps
import androidx.compose.material.icons.twotone.FlutterDash
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.RoomPreferences
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.ui.R
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

    object Overview: Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeDashboard,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_overview
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

    object Divider : Screen(
        type = ScreenType.Divider,
        item = ItemType.Divider,
        route = ScreenRoute.Divider,
        imageVector = Icons.Filled.Android,
        title = R.string.app_name
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
}