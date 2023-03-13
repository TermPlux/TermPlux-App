package io.termplux.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.viewpager2.widget.ViewPager2
import io.flutter.embedding.android.FlutterFragment
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.adapter.MainAdapter
import io.termplux.ui.navigation.Screen
import io.termplux.ui.screen.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityMain(
    viewPager: ViewPager2,
    activity: FragmentActivity,
    flutter: FlutterFragment,
    viewPagerAdapter: (FragmentActivity, FlutterFragment, ViewPager2, () -> Unit) -> MainAdapter,

    onOptionsMenu: () -> Unit,
    onToggle: () -> Unit,
    androidVersion: String,
    shizukuVersion: String,

    infoApp: (String) -> Unit,
    deleteApp: (String) -> Unit,


    targetAppVersionName: String,

    dynamicColorChecked: Boolean,
    taskBarChecked: Boolean,
    onDynamicChecked: (Boolean) -> Unit,
    onTaskBarChecked: (Boolean) -> Unit,
    onTaskBarSettings: () -> Unit,
    onSystemSettings: () -> Unit,
    onDefaultLauncherSettings: () -> Unit,


    onEasterEgg: () -> Unit,
    onNotice: () -> Unit,
    onSource: () -> Unit,
    onDevGitHub: () -> Unit,
    onDevTwitter: () -> Unit,
    onTeamGitHub: () -> Unit
) {
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
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.app_name
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                route = Screen.Navigate.route
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
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            expandedMenu.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expandedMenu.value,
                        onDismissRequest = {
                            expandedMenu.value = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "更多")
                            },
                            onClick = {
                                onOptionsMenu()
                                expandedMenu.value = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.MoreHoriz,
                                    contentDescription = null
                                )
                            },
                            enabled = true
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = "操作栏")
                            },
                            onClick = {
                                onToggle()
                                expandedMenu.value = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Fullscreen,
                                    contentDescription = null
                                )
                            },
                            enabled = true
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(
                                route = screen.route
                            ) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.imageVector,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        label = {
                            Text(
                                stringResource(
                                    id = screen.title
                                )
                            )
                        },
                        alwaysShowLabel = false
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
                route = Screen.Navigate.route
            ) {
                ScreenNavigate(
                    navController = navController
                )
            }
            composable(
                route = Screen.Home.route
            ) {
               ScreenHome(
                   navController = navController,
                   viewPager = viewPager,
                   activity = activity,
                   flutter = flutter,
                   viewPagerAdapter = viewPagerAdapter
               )
            }
            composable(
                route = Screen.Dashboard.route
            ) {
                ScreenDashboard(
                    navController = navController,
                    scope = scope,
                    snackBarHostState = snackBarHostState,
                    androidVersion = androidVersion,
                    shizukuVersion = shizukuVersion
                )
            }
            composable(
                route = Screen.Manager.route
            ) {
                ScreenManager(
                    targetAppName = "",
                    targetAppPackageName = "",
                    targetAppDescription = "",
                    targetAppVersionName = "",
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
                    dynamicColorChecked = dynamicColorChecked,
                    taskBarChecked = taskBarChecked,
                    onUninstall = {
                        deleteApp(BuildConfig.APPLICATION_ID)
                    },
                    onDynamicChecked = { value ->
                        onDynamicChecked(value)
                    },
                    onTaskBarChecked = { value ->
                        onTaskBarChecked(value)
                    },
                    onTaskBarSettings = onTaskBarSettings,
                    onSystemSettings = onSystemSettings,
                    onDefaultLauncherSettings = onDefaultLauncherSettings
                )
            }
            composable(
                route = Screen.About.route
            ) {
                ScreenAbout(
                    scope = scope,
                    snackBarHostState = snackBarHostState,
                    infoApp = {
                        infoApp(BuildConfig.APPLICATION_ID)
                    },
                    onEasterEgg = onEasterEgg,
                    onNotice = onNotice,
                    onSource = onSource,
                    onDevGitHub = onDevGitHub,
                    onDevTwitter = onDevTwitter,
                    onTeamGitHub = onTeamGitHub
                )
            }
        }
    }
}


/**
 * 主页面预览
 */
@Preview
@Composable
private fun ActivityMainPreview() {
    ActivityMain(
        viewPager = ViewPager2(LocalContext.current),
        activity = FragmentActivity(),
        flutter = FlutterFragment(),
        viewPagerAdapter = { activity, flutter, viewPager, settings ->
            MainAdapter(
                activity = activity,
                flutter = flutter,
                viewPager = viewPager,
                settings = settings
            )
        },
        onOptionsMenu = {},
        onToggle = {},
        androidVersion = "Android 13",
        shizukuVersion = "Shizuku 13",

        infoApp = {},
        deleteApp = {},
        targetAppVersionName = "",

        dynamicColorChecked = true,
        taskBarChecked = true,
        onDynamicChecked = {},
        onTaskBarChecked = {},
        onTaskBarSettings = {},
        onSystemSettings = {},
        onDefaultLauncherSettings = {},


        onEasterEgg = {},
        onNotice = {},
        onSource = {},
        onDevGitHub = {},
        onDevTwitter = {},
        onTeamGitHub = {}
    )
}