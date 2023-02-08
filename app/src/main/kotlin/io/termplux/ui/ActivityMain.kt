package io.termplux.ui

import android.content.pm.ResolveInfo
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.kongzue.dialogx.dialogs.PopTip
import io.termplux.R
import io.termplux.ui.screen.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityMain(
    gridView: GridView,
    appsList: List<ResolveInfo>,
    isSystemApps: (String) -> Boolean,
    startApp: (String, String) -> Unit,
    infoApp: (String) -> Unit,
    deleteApp: (String) -> Unit,



    targetAppVersionName: String,

    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
) {
    val items = listOf(
        Screen.Home,
        Screen.Apps,
        Screen.Dashboard,
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
            TopAppBar(
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
                colors = TopAppBarDefaults.smallTopAppBarColors(),
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
                    scope = scope,
                    snackBarHostState = snackBarHostState
                )
            }
            composable(
                route = Screen.Apps.route
            ) {
                ScreenApps(
                    navController = navController,
                    scope = scope,
                    snackBarHostState = snackBarHostState,
                    gridView = gridView,
                    appsList = appsList,
                    isSystemApps = { packageName ->
                        isSystemApps(packageName)
                    },
                    startApp = { packageName, className ->
                        startApp(packageName, className)
                    },
                    infoApp = { packageName ->
                        infoApp(packageName)
                    },
                    deleteApp = { packageName ->
                        deleteApp(packageName)
                    }
                )
            }
            composable(
                route = Screen.Dashboard.route
            ) {
                ScreenDashboard(
                    targetAppName = "",
                    targetAppPackageName = "",
                    targetAppDescription = "",
                    targetAppVersionName = targetAppVersionName,
                    NavigationOnClick = NavigationOnClick,
                    MenuOnClick = MenuOnClick,
                    SearchOnClick = SearchOnClick,
                    SheetOnClick = SheetOnClick,
                    AppsOnClick = AppsOnClick,
                    SelectOnClick = SelectOnClick,
                    onNavigateToApps = {},
                )
            }
            composable(
                route = Screen.Settings.route
            ) {
                ScreenSettings(
                    navController = navController,
                    scope = scope,
                    snackBarHostState = snackBarHostState,
                    dynamicColorChecked = true,
                    taskBarChecked = true,
                    onDynamicChecked = { b ->
                        PopTip.show(b.toString())
                    },
                    onTaskBarChecked = { b ->
                        PopTip.show(b.toString())
                    },
                    onTaskBarSettings = {},
                    onDefaultLauncherSettings = {}
                )
            }
            composable(
                route = Screen.About.route
            ) {
                ScreenAbout()
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
        gridView = GridView(LocalContext.current),
        appsList = ArrayList(),
        isSystemApps = { true },
        startApp = { _, _ -> },
        infoApp = {},
        deleteApp = {},
        targetAppVersionName = "",

        NavigationOnClick = { /*TODO*/ },
        MenuOnClick = { /*TODO*/ },
        SearchOnClick = { /*TODO*/ },
        SheetOnClick = { /*TODO*/ },
        AppsOnClick = { /*TODO*/ }) {

    }
}