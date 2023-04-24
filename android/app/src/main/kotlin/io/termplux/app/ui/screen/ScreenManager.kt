package io.termplux.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.preview.TermPluxPreviews
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager(
    navController: NavHostController,
    drawerState: DrawerState,
    tabBar: @Composable (modifier: Modifier) -> Unit,
    toggle: () -> Unit,
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val expandedPowerButton = remember {
        mutableStateOf(
            value = true
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.menu_manager
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = innerPadding
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    )
                    .verticalScroll(
                        state = scrollState
                    )
            ) {
                tabBar(
                    modifier = Modifier.fillMaxWidth()
                )
                ElevatedCard(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 5.dp,
                        bottom = 8.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .clickable {

                            }
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Search...",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        IconButton(
                            onClick = {
                                toggle()
                            },
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .wrapContentWidth(
                                    align = Alignment.End
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.TouchApp,
                                contentDescription = null
                            )
                        }
                    }
                }




//                AndroidView(
//                    factory = { context ->
//                        ClockView(
//                            context = context
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )


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


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        ),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Apps,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SelectAll,
                                contentDescription = null
                            )
                        }
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
    }

}

@Composable
@TermPluxPreviews
private fun ScreenManagerPreview() {
    ScreenManager(
        navController = rememberNavController(),
        drawerState = rememberDrawerState(
            initialValue = DrawerValue.Closed
        ),
        tabBar = { modifier ->
            Text(
                text = stringResource(
                    id = R.string.tab_layout_preview
                ),
                modifier = modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        },
        toggle = {},
        targetAppName = stringResource(
            id = R.string.app_name
        ),
        targetAppPackageName = BuildConfig.APPLICATION_ID,
        targetAppDescription = stringResource(
            id = R.string.app_description
        ),
        targetAppVersionName = BuildConfig.VERSION_NAME,
        NavigationOnClick = {},
        MenuOnClick = {},
        SearchOnClick = {},
        SheetOnClick = {},
        AppsOnClick = {},
        SelectOnClick = {},
        onNavigateToApps = {}
    )
}