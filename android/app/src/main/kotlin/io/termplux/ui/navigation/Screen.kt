package io.termplux.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.R

sealed class Screen(
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int
) {

    object Navigate : Screen(
        route = routeNavigation,
        imageVector = Icons.TwoTone.Navigation,
        title = R.string.menu_navigation
    )

    object Home : Screen(
        route = routeHome,
        imageVector = Icons.TwoTone.Home,
        title = R.string.menu_home
    )

    object Dashboard : Screen(
        route = routeDashboard,
        imageVector = Icons.TwoTone.Dashboard,
        title = R.string.menu_dashboard
    )

    object Manager : Screen(
        route = routeManager,
        imageVector = Icons.TwoTone.AppSettingsAlt,
        title = R.string.menu_manager
    )

    object Settings : Screen(
        route = routeSettings,
        imageVector = Icons.TwoTone.Settings,
        title = R.string.menu_settings
    )

    object About : Screen(
        route = routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )
}