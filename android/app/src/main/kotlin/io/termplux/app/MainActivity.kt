package io.termplux.app

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.kongzue.baseframework.util.JumpParameter
import io.termplux.R
import io.termplux.app.ui.ActivityMain
import io.termplux.app.ui.preview.TermPluxPreviews
import io.termplux.basic.activity.TermPluxActivity

class MainActivity : TermPluxActivity() {

    override fun onCreated(parameter: JumpParameter?) {
        setContents { navController,
                      windowSize,
                      displayFeatures,
                      content,
                      event,
                      message,
                      current,
                      browser ->
            ActivityMain(
                navController = navController,
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                pager = { modifier ->
                    content(
                        content = pager,
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    @TermPluxPreviews
    private fun ActivityMainPreview() {
        ActivityMain(
            navController = rememberNavController(),
            windowSize = WindowSizeClass.calculateFromSize(
                size = DpSize(
                    width = 400.dp,
                    height = 900.dp
                )
            ),
            displayFeatures = emptyList(),
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