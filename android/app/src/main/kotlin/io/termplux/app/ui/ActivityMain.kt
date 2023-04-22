package io.termplux.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.navigation.ScreenType
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.app.ui.screen.*
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ActivityMain(
    navController: NavHostController,
    topBar: @Composable (modifier: Modifier) -> Unit,
    pager: @Composable (modifier: Modifier) -> Unit,
    tabBar: @Composable (modifier: Modifier) -> Unit,
    optionsMenu: () -> Unit,
    androidVersion: String,
    shizukuVersion: String,
    current: (item: Int) -> Unit,
    toggle: () -> Unit
) {
    val pages = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Content,
        Screen.HomeFragment,
        Screen.LauncherFragment,
        Screen.NavigationFragment,
        Screen.SettingsFragment,
        Screen.Settings,
        Screen.About
    )
    val items = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Content,
        Screen.Settings,
    )
    val expandedMenu = remember {
        mutableStateOf(
            value = false
        )
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //  verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.app_description
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuOpen,
                                contentDescription = null
                            )
                        }
                    }

//                    ExtendedFloatingActionButton(
//                        text = {
//                            Text(
//                                text = stringResource(
//                                    id = R.string.app_name
//                                )
//                            )
//                        },
//                        icon = {
//                            Icon(
//                                imageVector = Icons.Outlined.Terminal,
//                                contentDescription = null,
//                            )
//                        },
//                        onClick = {
//                            scope.launch {
//                                drawerState.close()
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(
//                                horizontal = 16.dp,
//                                vertical = 10.dp
//                            )
//                    )
                }
                Divider()
                Column(
                    modifier = Modifier.verticalScroll(
                        state = rememberScrollState()
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(
                        modifier = Modifier.height(
                            height = 12.dp
                        )
                    )
                    pages.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.imageVector,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(
                                        id = item.title
                                    )
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                when (item.type) {
                                    ScreenType.Compose -> navController.navigate(
                                        route = item.route
                                    ) {
                                        popUpTo(
                                            id = navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }.also {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }

                                    ScreenType.Fragment -> current(
                                        item.route.toInt()
                                    ).also {
                                        navController.navigate(
                                            route = Screen.Content.route
                                        ) {
                                            popUpTo(
                                                id = navController.graph.findStartDestination().id
                                            ) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }.also {
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.padding(
                                paddingValues = NavigationDrawerItemDefaults.ItemPadding
                            )
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(
                            height = 12.dp
                        )
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(
                        modifier = Modifier.statusBarsPadding()
                    )
                    topBar(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = stringResource(
//                                id = R.string.app_name
//                            )
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    navigationIcon = {
//                        IconButton(
//                            onClick = {
//                                scope.launch {
//                                    drawerState.open()
//                                }
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Menu,
//                                contentDescription = null
//                            )
//                        }
//                    },
//                    actions = {
//                        IconButton(
//                            onClick = {
//                                expandedMenu.value = true
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.MoreVert,
//                                contentDescription = null
//                            )
//                        }
//                        DropdownMenu(
//                            expanded = expandedMenu.value,
//                            onDismissRequest = {
//                                expandedMenu.value = false
//                            }
//                        ) {
//                            DropdownMenuItem(
//                                text = {
//                                    Text(text = "更多")
//                                },
//                                onClick = {
//                                    optionsMenu()
//                                    expandedMenu.value = false
//                                },
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.Filled.MoreHoriz,
//                                        contentDescription = null
//                                    )
//                                },
//                                enabled = true
//                            )
//                            DropdownMenuItem(
//                                text = {
//                                    Text(text = "全屏")
//                                },
//                                onClick = {
//                                    toggle()
//                                    expandedMenu.value = false
//                                },
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.Filled.TouchApp,
//                                        contentDescription = null
//                                    )
//                                },
//                                enabled = true
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(),
//                    scrollBehavior = scrollBehavior
//                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
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
                    modifier = Modifier.fillMaxWidth(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(
                                    route = Screen.Content.route
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
                                imageVector = Icons.Outlined.Terminal,
                                contentDescription = null
                            )
                        }
//                        ExtendedFloatingActionButton(
//                            text = {
//                                Text(
//                                    text = stringResource(
//                                        id = R.string.menu_content
//                                    )
//                                )
//                            },
//                            icon = {
//                                Icon(
//                                    imageVector = Icons.Outlined.Terminal,
//                                    contentDescription = null
//                                )
//                            },
//                            onClick = {
//                                navController.navigate(
//                                    route = Screen.Content.route
//                                ) {
//                                    popUpTo(
//                                        id = navController.graph.findStartDestination().id
//                                    ) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            }
//                        )
                    }
                )
//                NavigationBar(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    items.forEach { item ->
//                        NavigationBarItem(
//                            selected = currentDestination?.hierarchy?.any {
//                                it.route == item.route
//                            } == true,
//                            onClick = {
//                                if (item.type == ScreenType.Compose) navController.navigate(
//                                    route = item.route
//                                ) {
//                                    popUpTo(
//                                        id = navController.graph.findStartDestination().id
//                                    ) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            },
//                            icon = {
//                                Icon(
//                                    imageVector = item.imageVector,
//                                    contentDescription = null
//                                )
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            enabled = true,
//                            label = {
//                                Text(
//                                    stringResource(
//                                        id = item.title
//                                    )
//                                )
//                            },
//                            alwaysShowLabel = false
//                        )
//                    }
//                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState
                )
            },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    )
                    .padding(
                        paddingValues = innerPadding
                    )
            ) {
                composable(
                    route = Screen.Home.route
                ) {
                    ScreenHome(
                        navController = navController,
                        scope = scope,
                        snackBarHostState = snackBarHostState,
                        androidVersion = androidVersion,
                        shizukuVersion = shizukuVersion
                    )
                }
                composable(
                    route = Screen.Dashboard.route
                ) {
                    ScreenDashboard(
                        navController = navController,
                        tabBar = tabBar,
                        toggle = toggle,
                        targetAppName = stringResource(id = R.string.app_name),
                        targetAppPackageName = BuildConfig.APPLICATION_ID,
                        targetAppDescription = stringResource(id = R.string.app_description),
                        targetAppVersionName = BuildConfig.VERSION_NAME,
                        NavigationOnClick = { /*TODO*/ },
                        MenuOnClick = { /*TODO*/ },
                        SearchOnClick = { /*TODO*/ },
                        SheetOnClick = { /*TODO*/ },
                        AppsOnClick = { /*TODO*/ },
                        SelectOnClick = { /*TODO*/ }) {

                    }
                }
                composable(
                    route = Screen.Content.route
                ) {
                    ScreenContent(
                        pager = pager
                    )
                }
                composable(
                    route = Screen.Settings.route
                ) {
                    ScreenSettings(
                        navController = navController,
                        scope = scope,
                        snackBarHostState = snackBarHostState,
                        current = current,
                        onTaskBarSettings = {},
                        onSystemSettings = {},
                        onDefaultLauncherSettings = {}
                    )
                }
                composable(
                    route = Screen.About.route
                ) {
                    ScreenAbout(
                        scope = scope,
                        snackBarHostState = snackBarHostState,
                        onEasterEgg = {},
                        onNotice = {},
                        onSource = {},
                        onDevGitHub = {},
                        onDevTwitter = {},
                        onTeamGitHub = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@TermPluxPreviews
fun ActivityMainPreview() {
    ActivityMain(
        navController = rememberNavController(),
        topBar = { modifier ->
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.toolbar_preview
                        )
                    )
                },
                modifier = modifier,
            )
        },
        pager = { modifier ->
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(
                        id = R.string.view_pager_preview
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
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
        optionsMenu = {},
        androidVersion = "Android Tiramisu 13.0",
        shizukuVersion = "Shizuku 13",
        current = {},
        toggle = {}
    )
}