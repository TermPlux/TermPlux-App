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
import io.termplux.app.ui.navigation.ItemType
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.navigation.ScreenType
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.app.ui.screen.*
import kotlinx.coroutines.launch

@Composable
fun ActivityMain(
    navController: NavHostController,
    drawerState: DrawerState,
    pager: @Composable (modifier: Modifier) -> Unit,
    tabBar: @Composable (modifier: Modifier) -> Unit,
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
        Screen.Manager,
        Screen.Settings,
        Screen.About,

        Screen.Divider,
        Screen.FragmentTitle,

        Screen.LauncherFragment,
        Screen.HomeFragment,
        Screen.AppsFragment,
        Screen.NavigationFragment,
        Screen.SettingsFragment
    )
    val items = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Manager,
        Screen.Settings
    )
    val expandedMenu = remember {
        mutableStateOf(
            value = false
        )
    }

//    val drawerState = rememberDrawerState(
//        initialValue = DrawerValue.Closed
//    )
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
//                // 头布局
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(
//                                all = 16.dp
//                            ),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = stringResource(
//                                id = R.string.app_description
//                            ),
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        IconButton(
//                            onClick = {
//                                scope.launch {
//                                    drawerState.close()
//                                }
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.MenuOpen,
//                                contentDescription = null
//                            )
//                        }
//                    }
//                }
//                ExtendedFloatingActionButton(
//                    text = {
//                        Text(text = stringResource(id = R.string.app_name))
//                    },
//                    icon = {
//                        Icon(imageVector = Icons.Outlined.Terminal, contentDescription = null)
//                    },
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 5.dp)
//                )
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
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }

                                        ScreenType.Fragment -> current(
                                            item.route.toInt()
                                        ).also {
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
                                            }.also {
                                                scope.launch {
                                                    drawerState.close()
                                                }
                                            }
                                        }

                                        ScreenType.Title -> scope.launch {
                                            drawerState.close()
                                        }

                                        ScreenType.Divider -> scope.launch {
                                            drawerState.close()
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
        },
        gesturesEnabled = true
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(
                route = Screen.Home.route
            ) {
                ScreenHome(
                    pager = pager
                )

            }
            composable(
                route = Screen.Dashboard.route
            ) {
                ScreenDashboard(
                    navController = navController,
                    drawerState = drawerState,
                    androidVersion = androidVersion,
                    shizukuVersion = shizukuVersion
                )

            }
            composable(
                route = Screen.Manager.route
            ) {
                ScreenManager(
                    navController = navController,
                    drawerState = drawerState,
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
                route = Screen.Settings.route
            ) {
                ScreenSettings(
                    navController = navController,
                    drawerState = drawerState,
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
             //       scope = scope,
                    drawerState = drawerState,
                 //   snackBarHostState = snackBarHostState,
                    onEasterEgg = {},
                    onNotice = {},
                    onSource = {},
                    onDevGitHub = {},
                    onDevTwitter = {},
                    onTeamGitHub = {}
                )
            }
        }
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            bottomBar = {
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
}

@Composable
@TermPluxPreviews
fun ActivityMainPreview() {
    ActivityMain(
        navController = rememberNavController(),
        drawerState = rememberDrawerState(
            initialValue = DrawerValue.Closed
        ),
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