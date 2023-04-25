package io.termplux.app.ui.navigation

import io.termplux.basic.adapter.ContentAdapter

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/TermPlux/TermPlux-App
 * 时间: 2023/04/23
 * 描述: Compose导航路由
 */
object ScreenRoute {
    const val routeHome: String = "home"
    const val routeDashboard: String = "dashboard"
    const val routeContent : String = "content"
    const val routeSettings: String = "settings"
    const val routeAbout: String = "about"

    const val routeHomeFragment: String = ContentAdapter.home.toString()
    const val routeLauncherFragment: String = ContentAdapter.launcher.toString()
    const val routeAppsFragment: String= ContentAdapter.apps.toString()
    const val routeSettingsFragment: String = ContentAdapter.settings.toString()

    const val Divider: String = "divider"
    const val Title: String = "title"
}