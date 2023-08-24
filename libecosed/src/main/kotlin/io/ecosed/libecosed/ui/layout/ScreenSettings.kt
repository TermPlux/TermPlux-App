package io.ecosed.libecosed.ui.layout

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
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.AppUtils
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.screen.Screen
import io.ecosed.libecosed.ui.theme.LibEcosedTheme

import io.ecosed.libecosed.ui.widget.SettingsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun ScreenSettings(
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
            OutlinedCard(
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 12.dp,
                    end = 12.dp,
                    bottom = 6.dp
                )
            ) {
                SettingsItem(
                    icon = Icons.Filled.RoomPreferences,
                    title = "首选项",
                    summary = "配置 Ecosed Framework"
                ) {
                    current(
                        Screen.Preference.route.toInt()
                    ).run {
                        navController.navigate(
                            route = Screen.Home.route
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
                    AppUtils.uninstallApp(AppUtils.getAppPackageName())
                }
            }
            OutlinedCard(
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 6.dp,
                    end = 12.dp,
                    bottom = 6.dp
                )
            ) {
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
            OutlinedCard(
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 6.dp,
                    end = 12.dp,
                    bottom = 12.dp
                )
            ) {
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

@ScreenPreviews
@Composable
private fun ScreenSettingsPreview() {
    LibEcosedTheme {
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