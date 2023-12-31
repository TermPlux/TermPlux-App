package io.termplux.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.ui.R
import io.termplux.ui.preview.ScreenPreviews
import io.termplux.ui.theme.TermPluxAppTheme

@Composable
fun ScreenManager(
    navController: NavHostController,
    toggle: () -> Unit,
    current: (item: Int) -> Unit,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
    onNavigateToApps: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val expandedPowerButton = remember {
        mutableStateOf(
            value = true
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
        ) {
//            ElevatedCard(
//                modifier = Modifier.padding(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 5.dp,
//                    bottom = 8.dp
//                )
//            ) {
//                TopActionBar(
//                    modifier = Modifier.fillMaxWidth(),
//                    visible = topBarVisible,
//                    update = topBarUpdate
//                )
//            }
            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Apps,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        Spacer(
                            modifier = Modifier.size(
                                size = ButtonDefaults.IconSpacing
                            )
                        )
                        FilledIconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SelectAll,
                                contentDescription = null
                            )
                        }
                    }
                }

//                AssistChip(
//                    onClick = {
//
//                    },
//                    label = {
//                        Text(
//                            text = "切换到内容页",
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 5.dp),
//                    enabled = true,
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Outlined.Terminal,
//                            contentDescription = null,
//                            modifier = Modifier.size(
//                                AssistChipDefaults.IconSize
//                            ),
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                )
            }

//            ElevatedCard(
//                modifier = Modifier.padding(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 8.dp,
//                    bottom = 8.dp
//                )
//            ) {
//
//
//            }

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
                        .padding(
                            all = 5.dp
                        )
                        .heightIn(min = 70.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.custom_termplux_24
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(
                                width = 48.dp,
                                height = 48.dp
                            )
                            .padding(
                                top = 5.dp
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f)
                    ) {
                        Text(
                            text = targetAppName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = targetAppPackageName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = targetAppDescription,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = targetAppVersionName,
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .wrapContentWidth(Alignment.End)
                    )
                    Switch(
                        checked = expandedPowerButton.value,
                        onCheckedChange = {
                            expandedPowerButton.value = it
                        },
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .wrapContentWidth(Alignment.End),
                        enabled = true
                    )
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 5.dp
                        )
                ) {
                    Text(text = "运行环境电源")
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "启动运行环境",
                                color = Color.Green
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOn,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Green
                            )
                        }
                    )
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "停止运行环境",
                                color = Color.Red
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOff,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Red
                            )
                        }
                    )
                }
            }




//            Divider()
//
//
//
//            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "设备信息",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(
//                            start = 5.dp,
//                            end = 5.dp,
//                            top = 5.dp,
//                            bottom = 2.5.dp
//                        )
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(IntrinsicSize.Min)
//                        .padding(
//                            start = 5.dp,
//                            end = 5.dp,
//                            top = 2.5.dp,
//                            bottom = 2.5.dp
//                        ),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Android,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = "sb",
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                start = 5.dp,
//                            )
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(IntrinsicSize.Min)
//                        .padding(
//                            start = 5.dp,
//                            end = 5.dp,
//                            top = 2.5.dp,
//                            bottom = 2.5.dp
//                        ),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.PhoneAndroid,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = "sb",
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                start = 5.dp,
//                            )
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(IntrinsicSize.Min)
//                        .padding(
//                            start = 5.dp,
//                            end = 5.dp,
//                            top = 2.5.dp,
//                            bottom = 2.5.dp
//                        ),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.DesignServices,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = "sb",
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                start = 5.dp,
//                            )
//                    )
//                }
            //}
        }
    }
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = stringResource(
//                            id = R.string.menu_dashboard
//                        )
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            scope.launch {
//                                drawerState.open()
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = null
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
//                scrollBehavior = scrollBehavior
//            )
//        },
//        bottomBar = {
//            BottomAppBar(
//                actions = {
//                    IconButton(
//                        onClick = {
//                            navController.navigateUp()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = null
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            navController.navigate(
//                                route = Screen.Content.route
//                            ) {
//                                popUpTo(
//                                    id = navController.graph.findStartDestination().id
//                                ) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }.also {
//                                current(
//                                    ScreenRoute.routeLauncherFragment.toInt()
//                                ).also {
//                                    toggle()
//                                }
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.TouchApp,
//                            contentDescription = null
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//
//                        }
//                    ) {
//
//                    }
//                },
//                floatingActionButton = {
//                    FloatingActionButton(
//                        onClick = {
//                            navController.navigate(
//                                route = Screen.Content.route
//                            ) {
//                                popUpTo(
//                                    id = navController.graph.findStartDestination().id
//                                ) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }.also {
//                                current(
//                                    ScreenRoute.routeLauncherFragment.toInt()
//                                )
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Outlined.Terminal,
//                            contentDescription = null
//                        )
//                    }
//                }
//            )
//        },
//        snackbarHost = {
//            SnackbarHost(
//                hostState = snackBarHostState
//            )
//        },
//        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
//    ) { innerPadding ->
//
//    }

}

@Composable
@ScreenPreviews
private fun ScreenManagerPreview() {
    TermPluxAppTheme {
        ScreenManager(
            navController = rememberNavController(),
            toggle = {},
            current = {},
            targetAppName = "",
            targetAppPackageName = "",
            targetAppDescription = "",
            targetAppVersionName = "1",
            NavigationOnClick = {},
            MenuOnClick = {},
            SearchOnClick = {},
            SheetOnClick = {},
            AppsOnClick = {},
            SelectOnClick = {},
            onNavigateToApps = {}
        )
    }
}