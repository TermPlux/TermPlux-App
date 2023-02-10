package io.termplux.ui.screen

import android.content.pm.ResolveInfo
import android.widget.GridView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.ui.navigation.Screen
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
                    setOnItemLongClickListener { _, _, position, _ ->
                        // 获取目标应用包名
                        val packageName = appsList[position].activityInfo.packageName
                        // 获取目标应用类名
                        val className = appsList[position].activityInfo.name

                        PopMenu.show(
                            arrayOf(
                                context.getString(R.string.menu_open),
                                context.getString(R.string.menu_info),
                                context.getString(R.string.menu_delete)
                            )
                        )
                            .setOnMenuItemClickListener { _, _, index ->
                                when (index) {
                                    0 -> {
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
                                    1 -> {
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
                                    2 -> {
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
                                false
                            }
                            .onIconChangeCallBack = object : OnIconChangeCallBack<PopMenu?>(true) {
                            override fun getIcon(
                                dialog: PopMenu?,
                                index: Int,
                                menuText: String
                            ): Int {
                                return when (index) {
                                    0 -> R.drawable.baseline_open_in_new_24
                                    1 -> R.drawable.outline_info_24
                                    2 -> R.drawable.outline_delete_24
                                    else -> 0
                                }
                            }
                        }
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

@Preview
@Composable
fun ScreenAppsPreview() {
    ScreenApps(
        navController = rememberNavController(),
        scope = rememberCoroutineScope(),
        snackBarHostState = SnackbarHostState(),
        gridView = GridView(LocalContext.current),
        appsList = ArrayList(),
        isSystemApps = {
            true
        },
        onStartApp = { _, _ -> },
        onInfoApp = {},
        onDeleteApp = {}
    )
}