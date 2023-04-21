package io.termplux.app.ui.screen

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.AppUtils
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.app.ui.widget.SettingsItem
import io.termplux.basic.adapter.MainAdapter
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
                .verticalScroll(scrollState)
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
                    text = stringResource(id = R.string.app_category_title),
                    modifier = Modifier.padding(all = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                SettingsItem(
                    icon = Icons.Filled.OpenInNew,
                    title = "其他设置",
                    summary = "跳转其他设置界面"
                ) {
                    current(MainAdapter.settings)
                    navController.navigate(
                        route = Screen.Content.route
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
                // 关于
                SettingsItem(
                    icon = Icons.Filled.Info,
                    title = stringResource(id = R.string.about_title),
                    summary = stringResource(id = R.string.about_summary)
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
                    title = stringResource(id = R.string.uninstall_title),
                    summary = stringResource(id = R.string.uninstall_summary)
                ) {
                    AppUtils.uninstallApp(BuildConfig.APPLICATION_ID)
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
                    text = stringResource(id = R.string.desktop_category_title),
                    modifier = Modifier.padding(all = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                // 任务栏设置
                SettingsItem(
                    icon = Icons.Filled.AppSettingsAlt,
                    title = stringResource(id = R.string.taskbar_title),
                    summary = stringResource(id = R.string.taskbar_summary)
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        onTaskBarSettings()
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                context.getString(R.string.desktop_error)
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
                    text = stringResource(id = R.string.system_category_title),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                // 系统设置
                SettingsItem(
                    icon = Icons.Filled.Android,
                    title = stringResource(id = R.string.system_settings_title),
                    summary = stringResource(id = R.string.system_settings_summary)
                ) {
                    onSystemSettings()
                }
                // 默认主屏幕应用
                SettingsItem(
                    icon = Icons.Filled.Home,
                    title = stringResource(id = R.string.default_launcher_title),
                    summary = stringResource(id = R.string.default_launcher_summary)
                ) {
                    onDefaultLauncherSettings()
                }
            }
        }
    }
}

@TermPluxPreviews
@Composable
private fun ScreenSettingsPreview() {
    ScreenSettings(
        navController = rememberNavController(),
        scope = rememberCoroutineScope(),
        snackBarHostState = SnackbarHostState(),
        current = {},
        onTaskBarSettings = {},
        onSystemSettings = {},
        onDefaultLauncherSettings = {}
    )
}