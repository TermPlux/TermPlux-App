package io.termplux.app.ui.screen

import android.system.Os
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.preview.TermPluxPreviews

@Composable
fun ScreenDashboard(
    navController: NavHostController,
    shizukuVersion: String
) {
    val scrollState = rememberScrollState()
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
                    start = 16.dp, end = 16.dp, top = 5.dp, bottom = 8.dp
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
                                    id = navController.graph.findStartDestination().id
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
                            ), style = MaterialTheme.typography.titleMedium
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
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(
                                weight = 1f
                            )
                            .wrapContentWidth(
                                align = Alignment.End
                            )
                    )
                }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier.clickable { }
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                all = 24.dp
                            )
                    ) {

                        Text(
                            text = "应用版本",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = BuildConfig.VERSION_NAME,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 16.dp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.kernel_version),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = Os.uname().release,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 16.dp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.device_arch),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = Os.uname().machine,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 16.dp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.shizuku_version),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = shizukuVersion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.android_version),
//                            body = androidVersion
//                        )
//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.shizuku_version),
//                            body = shizukuVersion
//                        )
//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.kernel_version),
//                            body = Os.uname().release
//                        )
//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.system_version),
//                            body = Os.uname().version
//                        )
//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.device_arch),
//                            body = Os.uname().machine
//                        )
//                        InfoItem(
//                            contents = contents,
//                            title = stringResource(id = R.string.device_code),
//                            body = Build.DEVICE
//


//                        TextButton(
//                            onClick = {
//
//                            },
//                            modifier = Modifier.align(
//                                alignment = Alignment.End
//                            )
//                        ) {
//                            Text(
//                                text = "全部参数"
//                            )
//                        }


//                        TextButton(
//                            onClick = {
//                                val clipboardManager = context.getSystemService(
//                                    Context.CLIPBOARD_SERVICE
//                                ) as ClipboardManager
//                                clipboardManager.setPrimaryClip(
//                                    ClipData.newPlainText(
//                                        context.getString(R.string.app_name), contents.toString()
//                                    )
//                                )
//                                scope.launch {
//                                    snackBarHostState.showSnackbar(
//                                        context.getString(R.string.copied_to_clipboard)
//                                    )
//                                }
//                            },
//                            modifier = Modifier.align(
//                                alignment = Alignment.End
//                            )
//                        ) {
//                            Text(
//                                text = stringResource(
//                                    id = R.string.copy_info
//                                )
//                            )
//                        }
                //    }
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                        .padding(
                            all = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = "支持开发",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 4.dp
                            )
                        )
                        Text(
                            text = "TermPlux 将保持免费和开源，向开发者捐赠以表示支持。",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                        .padding(
                            all = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = "了解 TermPlux",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 4.dp
                            )
                        )
                        Text(
                            text = "了解如何使用 TermPlux",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@TermPluxPreviews
@Composable
private fun ScreenDashboardPreview() {
    ScreenDashboard(
        navController = rememberNavController(),
        shizukuVersion = "13"
    )
}

