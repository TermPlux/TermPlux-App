package io.termplux.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.system.Os
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.ui.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenHome(
    navController: NavHostController,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    androidVersion: String,
    shizukuVersion: String
) {
    val scrollState = rememberScrollState()
    val contents = StringBuilder()
    val context = LocalContext.current

    @Composable
    fun Item(title: String, body: String) {
        contents.appendLine(
            value = title
        ).appendLine(
            value = body
        ).appendLine()
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(
            modifier = Modifier.height(
                height = 24.dp
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                route = Screen.Dashboard.route
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
                            all = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Terminal,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.padding(
                            start = 20.dp
                        )
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.app_description
                            ),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 4.dp
                            )
                        )
                        Text(
                            text = stringResource(
                                id = R.string.version
                            ) + ":\t" + BuildConfig.VERSION_NAME,
                            style = MaterialTheme.typography.bodyMedium
                        )
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 24.dp
                        )
                ) {
                    Item(
                        title = stringResource(id = R.string.android_version),
                        body = androidVersion
                    )
                    Item(
                        title = stringResource(id = R.string.shizuku_version),
                        body = shizukuVersion
                    )
                    Item(
                        title = stringResource(id = R.string.kernel_version),
                        body = Os.uname().release
                    )
                    Item(
                        title = stringResource(id = R.string.system_version),
                        body = Os.uname().version
                    )
                    Item(
                        title = stringResource(id = R.string.device_arch),
                        body = Os.uname().machine
                    )
                    Item(
                        title = stringResource(id = R.string.device_code),
                        body = Build.DEVICE
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            val clipboardManager = context.getSystemService(
                                Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    context.getString(R.string.app_name),
                                    contents.toString()
                                )
                            )
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    context.getString(R.string.copied_to_clipboard)
                                )
                            }
                        },
                        content = {
                            Text(
                                text = stringResource(id = R.string.copy_info)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScreenHomePreview() {
    ScreenHome(
        navController = rememberNavController(),
        scope = rememberCoroutineScope(),
        snackBarHostState = SnackbarHostState(),
        androidVersion = "13",
        shizukuVersion = "13"
    )
}