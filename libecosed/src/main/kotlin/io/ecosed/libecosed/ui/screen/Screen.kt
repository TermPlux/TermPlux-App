package io.ecosed.libecosed.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Apps
import androidx.compose.material.icons.twotone.Dashboard
import androidx.compose.material.icons.twotone.FlutterDash
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material.icons.twotone.Title
import androidx.compose.ui.graphics.vector.ImageVector
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.navigation.ItemType
import io.ecosed.libecosed.ui.navigation.ScreenRoute
import io.ecosed.libecosed.ui.navigation.ScreenType

internal sealed class Screen constructor(
    val type: ScreenType,
    val item: ItemType,
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int,
) {

    data object ComposeTitle: Screen(
        type = ScreenType.Title,
        item = ItemType.Title,
        route = ScreenRoute.Title,
        imageVector = Icons.TwoTone.Title,
        title = R.string.title_compose
    )

    data object Overview: Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeOverview,
        imageVector = Icons.TwoTone.Dashboard,
        title = R.string.menu_overview
    )

    data object Home : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeHome,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_home
    )

    data object Divider : Screen(
        type = ScreenType.Divider,
        item = ItemType.Divider,
        route = ScreenRoute.Divider,
        imageVector = Icons.Filled.Android,
        title = R.string.lib_name
    )

    data object Settings : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeSettings,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )

    data object About : Screen(
        type = ScreenType.Compose,
        item = ItemType.Default,
        route = ScreenRoute.routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )

    data object FragmentTitle: Screen(
        type = ScreenType.Title,
        item = ItemType.Title,
        route = ScreenRoute.Title,
        imageVector = Icons.TwoTone.Title,
        title = R.string.title_fragment
    )

    data object Main: Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = "0",
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_home
    )

    data object Apps: Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = "1",
        imageVector = Icons.TwoTone.Apps,
        title = R.string.menu_apps
    )

    data object Preference: Screen(
        type = ScreenType.Fragment,
        item = ItemType.Default,
        route = "2",
        imageVector = Icons.TwoTone.AppSettingsAlt,
        title = R.string.menu_preference
    )
}