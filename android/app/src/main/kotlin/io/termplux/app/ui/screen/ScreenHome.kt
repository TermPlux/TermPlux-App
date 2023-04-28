package io.termplux.app.ui.screen

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.navigation.ScreenType
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.app.ui.widget.HomeItem
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScreenHome(
    navController: NavHostController,
    currentDestination: NavDestination?,
    drawerState: DrawerState,
    androidVersion: String,
    shizukuVersion: String
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val contents = StringBuilder()
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val items = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Content,
        Screen.Settings
    )
    val expandedMenu = remember {
        mutableStateOf(
            value = false
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.menu_home
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
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
              //  tonalElevation = 8.dp
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true,
                        onClick = {
                            if (item.type == ScreenType.Compose) navController.navigate(
                                route = item.route
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
                        icon = {
                            Icon(
                                imageVector = item.imageVector,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        label = {
                            Text(
                                stringResource(
                                    id = item.title
                                )
                            )
                        },
                        alwaysShowLabel = true
                    )
                }
            }
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
                .nestedScroll(
                    connection = scrollBehavior.nestedScrollConnection
                )
                .padding(
                    paddingValues = innerPadding
                ),
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
                        start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                all = 24.dp
                            )
                    ) {
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.android_version),
                            body = androidVersion
                        )
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.shizuku_version),
                            body = shizukuVersion
                        )
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.kernel_version),
                            body = Os.uname().release
                        )
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.system_version),
                            body = Os.uname().version
                        )
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.device_arch),
                            body = Os.uname().machine
                        )
                        HomeItem(
                            contents = contents,
                            title = stringResource(id = R.string.device_code),
                            body = Build.DEVICE
                        )
                        TextButton(modifier = Modifier.align(Alignment.End), onClick = {
                            val clipboardManager = context.getSystemService(
                                Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    context.getString(R.string.app_name), contents.toString()
                                )
                            )
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    context.getString(R.string.copied_to_clipboard)
                                )
                            }
                        }, content = {
                            Text(
                                text = stringResource(id = R.string.copy_info)
                            )
                        })
                    }
                }
            }
        }
    }

}

@TermPluxPreviews
@Composable
private fun ScreenHomePreview() {
    ScreenHome(
        navController = rememberNavController(),
        currentDestination = null,
        drawerState = rememberDrawerState(
            initialValue = DrawerValue.Closed
        ),
        androidVersion = "13",
        shizukuVersion = "13"
    )
}

