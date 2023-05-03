package io.termplux.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import com.kongzue.baseframework.util.JumpParameter
import io.termplux.R
import io.termplux.app.ui.ActivityMain
import io.termplux.app.ui.navigation.Screen
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.app.ui.widget.window.ContentType
import io.termplux.app.ui.widget.window.NavigationType
import io.termplux.basic.activity.TermPluxActivity

class MainActivity : TermPluxActivity() {

    override fun onCreated(parameter: JumpParameter?) {
        setContents { navController,
                      drawerState,
                      navigationType,
                      contentType,
                      content,
                      event,
                      message,
                      current,
                      browser ->
            ActivityMain(
                navController = navController,
                drawerState = drawerState,
                navigationType = navigationType,
                contentType = contentType,
                pager = { modifier ->
                    content(
                        content = pager,
                        modifier = modifier
                    )
                },
                navBar = { modifier ->
                    content(
                        content = navBar,
                        modifier = modifier
                    )
                },
                tabRow = { modifier ->
                    content(
                        content = tabRow,
                        modifier = modifier
                    )
                },
                optionsMenu = {
                    event(options)
                },
                androidVersion = message(androidVersion),
                shizukuVersion = message(shizukuVersion),
                current = current,
                toggle = {
                    event(toggle)
                }
            )
        }
    }

    /**
     * 传入主页路由
     */
    override fun configViewPagerRoute(): String {
        return Screen.Home.route
    }

    /**
     * 传入设置路由
     */
    override fun configSettingsRoute(): String {
        return Screen.Settings.route
    }

    @Composable
    @TermPluxPreviews
    private fun ActivityMainPreview() {
        ActivityMain(
            navController = rememberNavController(),
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            ),
            navigationType = NavigationType.BottomNavigation,
            contentType = ContentType.Single,
            pager = { modifier ->
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.view_pager_preview
                        ), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            navBar = { modifier ->

            },
            tabRow = { modifier ->
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
}