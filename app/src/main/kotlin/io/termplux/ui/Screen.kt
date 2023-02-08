package io.termplux.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import io.termplux.R
import io.termplux.values.Navigation

sealed class Screen(
    val route: String,
    val imageVector: ImageVector,
    @StringRes val title: Int
) {

    object Navigate :
        Screen(
            route = Navigation.routeNavigation,
            imageVector = Icons.TwoTone.Navigation,
            title = R.string.menu_navigation
        )

    object Home :
        Screen(
            route = Navigation.routeHome,
            imageVector = Icons.TwoTone.Home,
            title = R.string.menu_home
        )

    object Apps :
        Screen(
            route = Navigation.routeLauncher,
            imageVector = Icons.TwoTone.RocketLaunch,
            title = R.string.menu_apps
        )

    object Dashboard :
        Screen(
            route = Navigation.routeDashboard,
            imageVector = Icons.TwoTone.Dashboard,
            title = R.string.menu_dashboard
        )

    object Settings :
        Screen(
            route = Navigation.routeSettings,
            imageVector = Icons.TwoTone.Settings,
            title = R.string.menu_settings
        )

    object About : Screen(
        route = Navigation.routeAbout,
        imageVector = Icons.TwoTone.Info,
        title = R.string.menu_about
    )
}