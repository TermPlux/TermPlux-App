package io.termplux.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.app.ui.navigation.ItemType
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.navigation.ScreenType
import io.termplux.app.ui.screen.*
import io.termplux.app.ui.widget.window.ContentType
import io.termplux.app.ui.widget.window.DevicePosture
import io.termplux.app.ui.widget.window.NavigationType
import io.termplux.app.ui.widget.window.isBookPosture
import io.termplux.app.ui.widget.window.isSeparating
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityMain(
    navController: NavHostController,
    drawerState: DrawerState,
    navigationType: NavigationType,
    contentType: ContentType,
    pager: @Composable (modifier: Modifier) -> Unit,
    navBar: @Composable (modifier: Modifier) -> Unit,
    tabRow: @Composable (modifier: Modifier) -> Unit,
    optionsMenu: () -> Unit,
    androidVersion: String,
    shizukuVersion: String,
    current: (item: Int) -> Unit,
    toggle: () -> Unit
) {

    val pages = listOf(
        Screen.ComposeTitle,
        Screen.Home,
        Screen.Dashboard,
        Screen.Content,
        Screen.Settings,
        Screen.About,
        Screen.Divider,
        Screen.FragmentTitle,
     //   Screen.LauncherFragment,
        Screen.HomeFragment,
        Screen.AppsFragment,
        Screen.SettingsFragment
    )
    val items = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Content,
        Screen.Settings
    )

    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentDestination = navBackStackEntry?.destination
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val navDrawer = remember {
        movableContentOf<PaddingValues> { innerPadding ->

        }
    }


    @Composable
    fun nav() {
        Column {
            // 头布局
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 16.dp
                        ),
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
                    AnimatedVisibility(
                        visible = navigationType != NavigationType.PermanentNavigationDrawer
                    ) {
                        IconButton(
                            onClick = {
                                if (
                                    navigationType != NavigationType.PermanentNavigationDrawer
                                ) {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuOpen,
                                contentDescription = null
                            )
                        }
                    }
                }
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(
                                id = R.string.app_name
                            )
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Terminal,
                            contentDescription = null
                        )
                    },
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
                        }.also {
                            if (
                                navigationType != NavigationType.PermanentNavigationDrawer
                            ) {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 5.dp
                        )
                )
            }
            // 导航列表
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
                    when (item.item) {
                        ItemType.Default -> NavigationDrawerItem(
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
                                        if (
                                            navigationType != NavigationType.PermanentNavigationDrawer
                                        ) {
                                            scope.launch {
                                                drawerState.close()
                                            }
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
                                            if (
                                                navigationType != NavigationType.PermanentNavigationDrawer
                                            ) {
                                                scope.launch {
                                                    drawerState.close()
                                                }
                                            }
                                        }
                                    }

                                    ScreenType.Title -> if (
                                        navigationType != NavigationType.PermanentNavigationDrawer
                                    ) {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }

                                    ScreenType.Divider -> if (
                                        navigationType != NavigationType.PermanentNavigationDrawer
                                    ) {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.padding(
                                paddingValues = NavigationDrawerItemDefaults.ItemPadding
                            )
                        )

                        ItemType.Title -> Text(
                            text = stringResource(
                                id = item.title
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    paddingValues = NavigationDrawerItemDefaults.ItemPadding
                                )
                                .padding(
                                    vertical = 5.dp
                                ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium
                        )

                        ItemType.Divider -> Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    paddingValues = NavigationDrawerItemDefaults.ItemPadding
                                )
                                .padding(
                                    vertical = 5.dp
                                )
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.height(
                        height = 12.dp
                    )
                )
            }
        }
    }

    @Composable
    fun content() {
        Row(
            modifier = Modifier
                .fillMaxSize()
//                .padding(
//                    paddingValues = innerPadding
//                )
        ) {
            AnimatedVisibility(
                visible = navigationType == NavigationType.NavigationRail
            ) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    header = {
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
                    }
                ) {
                    items.forEach { item ->
                        NavigationRailItem(
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
                            enabled = true,
                            label = {
                                Text(
                                    stringResource(
                                        id = item.title
                                    )
                                )
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    )
            ) {
                composable(
                    route = Screen.Home.route
                ) {
                    ScreenContent(
                        pager = pager,
                        navBar = navBar
                    )

                }
                composable(
                    route = Screen.Dashboard.route
                ) {
                    ScreenHome(
                        navController = navController,
                        shizukuVersion = shizukuVersion
                    )

                }
                composable(
                    route = Screen.Content.route
                ) {
                    ScreenDashboard(
                        navController = navController,
                        tabRow = tabRow,
                        toggle = toggle,
                        current = current,
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
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            topBar = {
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
//                        AnimatedVisibility(
//                            visible = navigationType != NavigationType.PermanentNavigationDrawer
//                        ) {
//                            IconButton(
//                                onClick = {
//                                    if (
//                                        navigationType != NavigationType.PermanentNavigationDrawer
//                                    ) {
//                                        scope.launch {
//                                            drawerState.open()
//                                        }
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Filled.Menu,
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    },
//                    actions = {
//                        IconButton(
//                            onClick = {
//
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.MoreVert,
//                                contentDescription = null
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(),
//                    scrollBehavior = scrollBehavior
//                )
//            },
//            bottomBar = {
//                AnimatedVisibility(
//                    visible = navigationType == NavigationType.BottomNavigation
//                ) {
//                    NavigationBar(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        items.forEach { item ->
//                            NavigationBarItem(
//                                selected = currentDestination?.hierarchy?.any {
//                                    it.route == item.route
//                                } == true,
//                                onClick = {
//                                    if (item.type == ScreenType.Compose) navController.navigate(
//                                        route = item.route
//                                    ) {
//                                        popUpTo(
//                                            id = navController.graph.findStartDestination().id
//                                        ) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
//                                },
//                                icon = {
//                                    Icon(
//                                        imageVector = item.imageVector,
//                                        contentDescription = null
//                                    )
//                                },
//                                enabled = true,
//                                label = {
//                                    Text(
//                                        stringResource(
//                                            id = item.title
//                                        )
//                                    )
//                                },
//                                alwaysShowLabel = false
//                            )
//                        }
//                    }
//                }
//            },
//            snackbarHost = {
//                SnackbarHost(
//                    hostState = snackBarHostState
//                )
//            },
//            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
//        ) { innerPadding ->
//
//        }


    }



    if (navigationType == NavigationType.PermanentNavigationDrawer) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    nav()
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    nav()
                }
            },
            modifier = Modifier.fillMaxSize(),
            drawerState = drawerState,
            gesturesEnabled = true
        ) {
            content()
        }
    }


}

