package io.ecosed.libecosed.ui.layout

import android.app.Activity
import android.system.Os
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.twotone.AppSettingsAlt
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.OpenInNew
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.screen.Screen
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.ui.widget.TopActionBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenOverview(
    navController: NavHostController,
    topBarVisible: Boolean,
    drawerState: DrawerState,
    topBarUpdate: (MaterialToolbar) -> Unit,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        height = 250.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(
                            weight = 1f,
                            fill = true
                        )
                ) {
                    Card(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 12.dp,
                                top = 12.dp,
                                end = 6.dp,
                                bottom = 6.dp
                            ),
//                        colors = CardDefaults.elevatedCardColors(
//                            containerColor = MaterialTheme.colorScheme.secondaryContainer
//                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    all = 24.dp
                                ),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Image(
                                painter = rememberDrawablePainter(
                                    drawable = AppUtils.getAppIcon()
                                ),
                                contentDescription = null
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(text = "666")
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(
                            weight = 1f,
                            fill = true
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(
                                weight = 1f,
                                fill = true
                            )
                    ) {
                        Card(
                            onClick = {

                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 6.dp,
                                    top = 12.dp,
                                    end = 12.dp,
                                    bottom = 6.dp
                                ),
//                            colors = CardDefaults.elevatedCardColors(
//                                containerColor = MaterialTheme.colorScheme.secondaryContainer
//                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        all = 24.dp
                                    ),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "应用名称",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(
                                            height = 4.dp
                                        )
                                )
                                Text(
                                    text = AppUtils.getAppName(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(
                                weight = 1f,
                                fill = true
                            )
                    ) {
                        Card(
                            onClick = {

                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 6.dp,
                                    top = 6.dp,
                                    end = 12.dp,
                                    bottom = 6.dp
                                ),
//                            colors = CardDefaults.elevatedCardColors(
//                                containerColor = MaterialTheme.colorScheme.secondaryContainer
//                            )
                        ) {

                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    ),
//                colors = CardDefaults.elevatedCardColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer
//                )
            ) {
                TopActionBar(
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(),
                    visible = topBarVisible,
                    drawerState = drawerState,
                    update = topBarUpdate
                )
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        start = 12.dp,
//                        top = 6.dp,
//                        end = 12.dp,
//                        bottom = 6.dp
//                    )
//                    .align(
//                        alignment = Alignment.CenterHorizontally
//                    )
//                    .horizontalScroll(
//                        state = rememberScrollState()
//                    ),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                FilledIconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Menu,
//                        contentDescription = null
//                    )
//                }
//                Spacer(
//                    modifier = Modifier.size(
//                        size = ButtonDefaults.IconSpacing
//                    )
//                )
//                FilledIconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.MoreVert,
//                        contentDescription = null
//                    )
//                }
//                Spacer(
//                    modifier = Modifier.size(
//                        size = ButtonDefaults.IconSpacing
//                    )
//                )
//                FilledIconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Apps,
//                        contentDescription = null
//                    )
//                }
//                Spacer(
//                    modifier = Modifier.size(
//                        size = ButtonDefaults.IconSpacing
//                    )
//                )
//                FilledIconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Search,
//                        contentDescription = null
//                    )
//                }
//                Spacer(
//                    modifier = Modifier.size(
//                        size = ButtonDefaults.IconSpacing
//                    )
//                )
//                FilledIconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.SelectAll,
//                        contentDescription = null
//                    )
//                }
//            }
            OutlinedCard(
                onClick = {
                    navController.navigate(
                        route = Screen.About.route
                    ) {
                        popUpTo(
                            id = navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 6.dp,
                    end = 12.dp,
                    bottom = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 24.dp
                        )
                ) {
                    Text(
                        text = AppUtils.getAppName(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                height = 24.dp
                            )
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                height = 24.dp
                            )
                    )
                    Row {
                        Column(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Text(
                                text = "版本名称",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppVersionName(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    height = 16.dp
                                )
                            )
                            Text(
                                text = "目标版本",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppTargetSdkVersion().toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Text(
                                text = "版本代码",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppVersionCode().toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    height = 16.dp
                                )
                            )
                            Text(
                                text = "最小版本",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppMinSdkVersion().toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 6.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        OutlinedIconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.OpenInNew,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        OutlinedIconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AppSettingsAlt,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        OutlinedIconButton(
                            onClick = {
                                navController.navigate(
                                    route = Screen.About.route
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
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        OutlinedIconButton(
                            onClick = {
                                AppUtils.launchAppDetailsSettings()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null
                            )
                        }
                    }
                }
            }



        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ScreenPreviews
@Composable
private fun ScreenOverviewPreview() {
    LibEcosedTheme {
        ScreenOverview(
            navController = rememberNavController(),
            shizukuVersion = "13",
            topBarVisible = true,
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            ),
            topBarUpdate = {}
        )
    }
}