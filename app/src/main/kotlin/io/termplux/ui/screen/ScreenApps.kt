package io.termplux.ui.screen

import android.content.pm.ResolveInfo
import android.widget.GridView
import android.widget.PopupMenu
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.ui.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenApps(
    navController: NavHostController,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    gridView: GridView,
    appsList: List<ResolveInfo>,
    isSystemApps: (String) -> Boolean,
    onStartApp: (String, String) -> Unit,
    onInfoApp: (String) -> Unit,
    onDeleteApp: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                gridView.apply {
                    setOnItemLongClickListener { _, view, position, _ ->
                        // 获取目标应用包名
                        val packageName = appsList[position].activityInfo.packageName
                        // 获取目标应用类名
                        val className = appsList[position].activityInfo.name
                        // 初始化弹出菜单
                        val popupMenu = PopupMenu(context, view)
                        // 加载菜单
                        popupMenu.inflate(R.menu.apps_more_menu)
                        // 点击事件
                        popupMenu.setOnMenuItemClickListener { item ->
                            when (item?.itemId) {
                                // 打开应用
                                R.id.open -> {
                                    if (packageName != BuildConfig.APPLICATION_ID) {
                                        onStartApp(packageName, className)
                                    } else {
                                        // 跳转主页
                                        navController.navigate(
                                            route = Screen.Home.route
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
                                }
                                // 跳转应用信息界面
                                R.id.info -> {
                                    if (packageName != BuildConfig.APPLICATION_ID) {
                                        onInfoApp(packageName)
                                    } else {
                                        // 跳转关于
                                        navController.navigate(
                                            route = Screen.About.route
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
                                }
                                // 卸载应用
                                R.id.delete -> {
                                    if (!isSystemApps(packageName)) {
                                        if (packageName != BuildConfig.APPLICATION_ID) {
                                            onDeleteApp(packageName)
                                        } else {
                                            scope.launch {
                                                snackBarHostState.showSnackbar(
                                                    context.getString(R.string.uninstall_tip)
                                                )
                                            }
                                            // 跳转设置
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
                                    } else {
                                        // 报错
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                context.getString(R.string.uninstall_error)
                                            )
                                        }
                                    }
                                }
                            }
                            true
                        }
                        popupMenu.show()
                        true
                    }

                    setOnItemClickListener { _, _, position, _ ->
                        // 获取目标应用包名
                        val packageName = appsList[position].activityInfo.packageName
                        // 获取目标应用类名
                        val className = appsList[position].activityInfo.name
                        // 判断是否为自身
                        if (packageName != BuildConfig.APPLICATION_ID) {
                            // 启动应用
                            onStartApp(packageName, className)
                        } else {
                            // 跳转主页
                            navController.navigate(
                                route = Screen.Home.route
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
                    }
                }
            }
        )
    }
}