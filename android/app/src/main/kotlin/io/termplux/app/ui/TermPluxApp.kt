package io.termplux.app.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.termplux.app.ui.navigation.ContentType
import io.termplux.app.ui.navigation.NavigationType
import kotlinx.coroutines.launch

@Composable
fun TermPluxApp(
    windowSize: WindowSizeClass
) {
    val navigationType: NavigationType
    val contentType: ContentType

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = ContentType.DUAL_PANE
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = ContentType.DUAL_PANE
        }
        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
    }

//    NavigationWrapper(
//        navigationType = navigationType,
//        contentType = contentType
//    )
}

//@Composable
//private fun NavigationWrapper(
//    navigationType: NavigationType,
//    contentType: ContentType
//) {
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    val navController = rememberNavController()
//    val navigationActions = remember(navController) {
//        NavigationActions(navController)
//    }
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val selectedDestination =
//        navBackStackEntry?.destination?.route ?: ReplyRoute.INBOX
//
//    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
//        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
//        PermanentNavigationDrawer(drawerContent = {
//            PermanentNavigationDrawerContent(
//                selectedDestination = selectedDestination,
//                navigationContentPosition = navigationContentPosition,
//                navigateToTopLevelDestination = navigationActions::navigateTo,
//            )
//        }) {
//            ReplyAppContent(
//                navigationType = navigationType,
//                contentType = contentType,
//                displayFeatures = displayFeatures,
//                navigationContentPosition = navigationContentPosition,
//                replyHomeUIState = replyHomeUIState,
//                navController = navController,
//                selectedDestination = selectedDestination,
//                navigateToTopLevelDestination = navigationActions::navigateTo,
//                closeDetailScreen = closeDetailScreen,
//                navigateToDetail = navigateToDetail
//            )
//        }
//    } else {
//        ModalNavigationDrawer(
//            drawerContent = {
//                ModalNavigationDrawerContent(
//                    selectedDestination = selectedDestination,
//                    navigationContentPosition = navigationContentPosition,
//                    navigateToTopLevelDestination = navigationActions::navigateTo,
//                    onDrawerClicked = {
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    }
//                )
//            },
//            drawerState = drawerState
//        ) {
//            ReplyAppContent(
//                navigationType = navigationType,
//                contentType = contentType,
//                displayFeatures = displayFeatures,
//                navigationContentPosition = navigationContentPosition,
//                replyHomeUIState = replyHomeUIState,
//                navController = navController,
//                selectedDestination = selectedDestination,
//                navigateToTopLevelDestination = navigationActions::navigateTo,
//                closeDetailScreen = closeDetailScreen,
//                navigateToDetail = navigateToDetail
//            ) {
//                scope.launch {
//                    drawerState.open()
//                }
//            }
//        }
//    }
//}