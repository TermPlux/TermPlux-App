package io.termplux.ui.screen

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.R
import io.termplux.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenSettings(
    navController: NavHostController,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    dynamicColorChecked: Boolean,
    taskBarChecked: Boolean,
    onUninstall: () -> Unit,
    onDynamicChecked: (Boolean) -> Unit,
    onTaskBarChecked: (Boolean) -> Unit,
    onTaskBarSettings: () -> Unit,
    onSystemSettings: () -> Unit,
    onDefaultLauncherSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val color = remember {
        mutableStateOf(value = true)
    }
    color.value = dynamicColorChecked
    val taskbar = remember {
        mutableStateOf(value = true)
    }
    taskbar.value = taskBarChecked
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_category_title),
                modifier = Modifier.padding(all = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            // 动态颜色
            Surface(

                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .toggleable(
                            value = color.value,
                            onValueChange = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    color.value = it
                                    onDynamicChecked(it)
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            context.getString(R.string.dynamic_colors_error)
                                        )
                                    }
                                }
                            },
                            role = Role.Switch
                        )
                        .padding(
                            horizontal = 16.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ColorLens,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.dynamic_colors_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.dynamic_colors_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = color.value,
                        onCheckedChange = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                color.value = it
                                onDynamicChecked(it)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        context.getString(R.string.dynamic_colors_error)
                                    )
                                }
                            }
                        }
                    )
                }
            }
            // 关于
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
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
                        .padding(
                            horizontal = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.about_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.about_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            }
            // 卸载
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onUninstall()
                        }
                        .padding(
                            horizontal = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.uninstall_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.uninstall_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            }
            Divider()
            Text(
                text = stringResource(id = R.string.desktop_category_title),
                modifier = Modifier.padding(all = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            // 完整桌面
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 70.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .toggleable(
                            value = taskbar.value,
                            onValueChange = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    taskbar.value = it
                                    onTaskBarChecked(it)
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            context.getString(R.string.desktop_error)
                                        )
                                    }
                                }
                            },
                            role = Role.Switch
                        )
                        .padding(
                            horizontal = 16.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LaptopChromebook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.desktop_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.desktop_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = taskbar.value,
                        onCheckedChange = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                taskbar.value = it
                                onTaskBarChecked(it)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        context.getString(R.string.desktop_error)
                                    )
                                }
                            }
                        }
                    )
                }
            }
            // 任务栏设置
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onTaskBarSettings()
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AppSettingsAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.taskbar_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.taskbar_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            }
            Divider()
            Text(
                text = stringResource(id = R.string.system_category_title),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            // 系统设置
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onSystemSettings()
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Android,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.system_settings_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.system_settings_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            }
            // 默认主屏幕应用
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onDefaultLauncherSettings()
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.default_launcher_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = R.string.default_launcher_summary),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScreenSettingsPreview() {
    ScreenSettings(
        navController = rememberNavController(),
        scope = rememberCoroutineScope(),
        snackBarHostState = SnackbarHostState(),
        dynamicColorChecked = true,
        taskBarChecked = true,
        onUninstall = {},
        onDynamicChecked = {},
        onTaskBarChecked = {},
        onTaskBarSettings = {},
        onSystemSettings = {},
        onDefaultLauncherSettings = {}
    )
}