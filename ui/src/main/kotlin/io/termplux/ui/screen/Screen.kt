package io.termplux.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Apps
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.RoomPreferences
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Terminal
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.ui.R
import io.termplux.ui.navigation.ItemType
import io.termplux.ui.navigation.ScreenRoute
import io.termplux.ui.navigation.ScreenType

sealed class Screen(
    val type: ScreenType,
    val item: ItemType,
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int,
) {

    data object Overview: Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeDashboard,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_overview
    )

    data object Home : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeHome,
        imageVector = Icons.TwoTone.Terminal,
        title = R.string.menu_home
    )

//    object Dashboard : Screen(
//        type = ScreenType.Compose,
//        item = ItemType.Default,
//        route = ScreenRoute.routeDashboard,
//        imageVector = Icons.TwoTone.List,
//        title = R.string.menu_overview
//    )

    data object Manager : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeManager,
        imageVector = Icons.TwoTone.AppSettingsAlt,
        title = R.string.menu_manager
    )

    data object Account : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = "account",
        imageVector = Icons.TwoTone.AccountCircle,
        title = R.string.account
    )

    data object Divider : Screen(
        type = ScreenType.Divider,
        item = ItemType.Divider,
        route = ScreenRoute.Divider,
        imageVector = Icons.Filled.Android,
        title = R.string.divider
    )

    data object Settings : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeSettings,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )

    data object Preference : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = "preference",
        imageVector = Icons.TwoTone.RoomPreferences,
        title = R.string.menu_preference
    )

    data object About : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )
}