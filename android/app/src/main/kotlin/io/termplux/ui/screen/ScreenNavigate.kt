package io.termplux.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.termplux.ui.navigation.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScreenNavigate(navController: NavHostController) {
    val items = listOf(
        Screen.Navigate,
        Screen.Home,
        Screen.Dashboard,
        Screen.Manager,
        Screen.Settings,
        Screen.About
    )
    val scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxSize(),
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.background,
                drawerTonalElevation = 0.dp
            ) {
                Spacer(
                    modifier = Modifier.height(
                        height = 12.dp
                    )
                )
                items.forEach { item ->
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
                            navController.navigate(
                                route = item.route
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
    }
}

@Preview
@Composable
private fun ScreenNavigationPreview() {
    ScreenNavigate(
        navController = rememberNavController()
    )
}