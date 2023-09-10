package io.ecosed.libecosed.ui.layout

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.navigation.ItemType
import io.ecosed.libecosed.ui.navigation.ScreenType
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.screen.Screen
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.ui.window.ContentType
import io.ecosed.libecosed.ui.window.DevicePosture
import io.ecosed.libecosed.ui.window.NavigationType
import io.ecosed.libecosed.ui.window.isBookPosture
import io.ecosed.libecosed.ui.window.isSeparating
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ActivityMain(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    productLogo: Drawable?,
    topBarVisible: Boolean,
    viewPager2: ViewPager2,

    topBarUpdate: (MaterialToolbar) -> Unit,

    androidVersion: String,
    shizukuVersion: String,
    current: (Int) -> Unit,
    toggle: () -> Unit,
    taskbar: () -> Unit,
    customTabs: (String) -> Unit,

) {
    val pages = listOf(
        Screen.ComposeTitle,
        Screen.Overview,
        Screen.Home,
        Screen.Settings,
        Screen.About,
        Screen.Divider,
        Screen.FragmentTitle,
        Screen.Main,
        Screen.Apps,
        Screen.Preference
    )
    val items = listOf(
        Screen.Overview,
        Screen.Home,
        Screen.Settings,
        Screen.About
    )

    val navController: NavHostController = rememberNavController()


    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentDestination = navBackStackEntry?.destination

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val drawerState: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val expanded = remember {
        mutableStateOf(value = false)
    }

    val navigationType: NavigationType
    val contentType: ContentType

    val foldingFeature =
        displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(
            foldFeature = foldingFeature
        ) -> DevicePosture.BookPosture(
            hingePosition = foldingFeature.bounds
        )

        isSeparating(
            foldFeature = foldingFeature
        ) -> DevicePosture.Separating(
            hingePosition = foldingFeature.bounds,
            orientation = foldingFeature.orientation
        )

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BottomNavigation
            contentType = ContentType.Single
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NavigationRail
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ContentType.Dual
            } else {
                ContentType.Single
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                NavigationType.NavigationRail
            } else {
                NavigationType.PermanentNavigationDrawer
            }
            contentType = ContentType.Dual
        }

        else -> {
            navigationType = NavigationType.BottomNavigation
            contentType = ContentType.Single
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
                            id = R.string.lib_description
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
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = R.string.lib_name
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                current(
                                    Screen.Main.route.toInt()
                                ).run {
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
                                    }
                                }
                            }
                        ) {
                            Image(
                                painter = rememberDrawablePainter(
                                    drawable = productLogo
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = {
                                expanded.value = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "关于")
                                },
                                onClick = {

                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null
                                    )
                                },
                                enabled = true
                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    route = Screen.Settings.route
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
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {
                                expanded.value = !expanded.value
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(),
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = (navigationType == NavigationType.BottomNavigation)
                ) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth()
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
                startDestination = Screen.Overview.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues = innerPadding
                    )
                    .nestedScroll(
                        connection = scrollBehavior.nestedScrollConnection
                    ),
                builder = {
                    composable(
                        route = Screen.Overview.route
                    ) {
                        ScreenOverview(
                            topBarVisible = topBarVisible,
                            drawerState = drawerState,
                            topBarUpdate = topBarUpdate,
                            navController = navController,
                            current = current,
                            shizukuVersion = shizukuVersion
                        )
                    }
                    composable(
                        route = Screen.Home.route
                    ) {
                        ScreenHome(
                            topBarVisible = topBarVisible,
                            drawerState = drawerState,
                            topBarUpdate = topBarUpdate,
                            navController = navController,
                            viewPager2 = viewPager2
                        )
                    }
                    composable(
                        route = Screen.Settings.route
                    ) {
                        ScreenSettings(
                            navController = navController,
                            //  navControllerFragment = subNavController,
                            scope = scope,
                            snackBarHostState = snackBarHostState,
                            current = current,
                            onTaskBarSettings = taskbar,
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
                            customTabs = customTabs
                        )
                    }
                }
            )
        }
    }

    when (navigationType) {
        NavigationType.PermanentNavigationDrawer -> PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    nav()
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }




//        NavigationType.NavigationRail,
//        NavigationType.BottomNavigation -> ModalNavigationDrawer(
//            drawerContent = {
//                ModalDrawerSheet {
//                    nav()
//                }
//            },
//            modifier = Modifier.fillMaxSize(),
//            drawerState = drawerState,
//            gesturesEnabled = true
//        ) {
//            Row(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                AnimatedVisibility(
//                    visible = navigationType == NavigationType.NavigationRail
//                ) {
//                    NavigationRail(
//                        modifier = Modifier.fillMaxHeight(),
//                        header = {
//                            FloatingActionButton(
//                                onClick = {
//
//                                }
//                            ) {
//                                Image(
//                                    painter = rememberDrawablePainter(
//                                        drawable = productLogo
//                                    ),
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    ) {
//                        Column(
//                            modifier = Modifier.verticalScroll(
//                                state = rememberScrollState()
//                            )
//                        ) {
//                            items.forEach { item ->
//                                NavigationRailItem(
//                                    selected = currentDestination?.hierarchy?.any {
//                                        it.route == item.route
//                                    } == true,
//                                    onClick = {
//                                        if (item.type == ScreenType.Compose) navController.navigate(
//                                            route = item.route
//                                        ) {
//                                            popUpTo(
//                                                id = navController.graph.findStartDestination().id
//                                            ) {
//                                                saveState = true
//                                            }
//                                            launchSingleTop = true
//                                            restoreState = true
//                                        }
//                                    },
//                                    icon = {
//                                        Icon(
//                                            imageVector = item.imageVector,
//                                            contentDescription = null
//                                        )
//                                    },
//                                    enabled = true,
//                                    label = {
//                                        Text(
//                                            stringResource(
//                                                id = item.title
//                                            )
//                                        )
//                                    },
//                                    alwaysShowLabel = false
//                                )
//                            }
//                        }
//                    }
//                }
//                content()
//            }
//        }


        NavigationType.NavigationRail,
        NavigationType.BottomNavigation -> Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = navigationType == NavigationType.NavigationRail
            ) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    header = {
                        FloatingActionButton(
                            onClick = {

                            }
                        ) {
                            Image(
                                painter = rememberDrawablePainter(
                                    drawable = productLogo
                                ),
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState()
                        )
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
            }
            content()
        }
    }
}

@ScreenPreviews
@Composable
private fun ActivityMainPreview() {
    LibEcosedTheme {

    }
}