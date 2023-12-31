package io.termplux.ui.layout

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RoomPreferences
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.AppUtils
import io.termplux.ui.R
import io.termplux.ui.preview.ScreenPreviews
import io.termplux.ui.screen.Screen
import io.termplux.ui.theme.TermPluxAppTheme
import io.termplux.ui.widget.SettingsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenSettings(
    navController: NavHostController,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    current: (Int) -> Unit,
    onTaskBarSettings: () -> Unit,
    onSystemSettings: () -> Unit,
    onDefaultLauncherSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
        ) {
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 5.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = "app",
                    modifier = Modifier.padding(all = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                SettingsItem(
                    icon = Icons.Filled.RoomPreferences,
                    title = "首选项",
                    summary = "配置 TermPlux"
                ) {
                    navController.navigate(
                        route = Screen.Preference.route
                    ) {
                        popUpTo(
                            id = navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                // 关于
                SettingsItem(
                    icon = Icons.Filled.Info,
                    title = "关于",
                    summary = "关于"
                ) {
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
                // 卸载
                SettingsItem(
                    icon = Icons.Filled.Delete,
                    title = "卸载",
                    summary = "卸载"
                ) {
                    AppUtils.uninstallApp(AppUtils.getAppPackageName())
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = "桌面",
                    modifier = Modifier.padding(all = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                // 任务栏设置
                SettingsItem(
                    icon = Icons.Filled.AppSettingsAlt,
                    title = "taskbar",
                    summary = "taskbar"
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        onTaskBarSettings()
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
//                                context.getString(R.string.desktop_error)
                                "desktop_error"
                            )
                        }
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp
                )
            ) {
                Text(
                    text = "system",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                // 系统设置
                SettingsItem(
                    icon = Icons.Filled.Android,
                    title = "设置",
                    summary = "设置"
                ) {
                    onSystemSettings()
                }
                // 默认主屏幕应用
                SettingsItem(
                    icon = Icons.Filled.Home,
                    title = "桌面",
                    summary = "桌面"
                ) {
                    onDefaultLauncherSettings()
                }
            }
        }
    }
}

@ScreenPreviews
@Composable
private fun ScreenSettingsPreview() {
    TermPluxAppTheme {
        ScreenSettings(
            navController = rememberNavController(),
            scope = rememberCoroutineScope(),
            snackBarHostState = remember {
                SnackbarHostState()
            },
            current = {},
            onTaskBarSettings = {},
            onSystemSettings = {},
            onDefaultLauncherSettings = {}
        )
    }
}