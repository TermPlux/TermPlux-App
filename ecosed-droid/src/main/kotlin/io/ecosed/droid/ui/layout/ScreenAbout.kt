package io.ecosed.droid.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.ecosed.droid.R
import io.ecosed.droid.ui.preview.ScreenPreviews
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.utils.EcosedUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenAbout(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onEasterEgg: () -> Unit,
    onNotice: () -> Unit,
    customTabs: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    val position: MutableState<Int> = remember {
        mutableIntStateOf(value = 0)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 6.dp
                    )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                all = 24.dp
                            )
                            .height(
                                height = 48.dp
                            )
                    ) {
                        Image(
                            painter = rememberDrawablePainter(
                                drawable = AppUtils.getAppIcon()
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 16.dp
                                ),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(
                                text = AppUtils.getAppName(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                            .clickable {
                                AppUtils.launchAppDetailsSettings()
                            }
                            .padding(
                                horizontal = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 5.dp
                                )
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.version
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppVersionName(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                            .clickable {
                                when (position.value) {
                                    0 -> {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                "喵~"
                                            )
                                        }
                                        position.value++
                                    }

                                    1 -> {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                "喵喵喵?"
                                            )
                                        }
                                        position.value++
                                    }

                                    2 -> {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                "🍥🍥🍥"
                                            )
                                        }
                                        position.value++
                                    }

                                    else -> {
                                        onEasterEgg()
                                        position.value = 0
                                    }
                                }
                            }
                            .padding(
                                horizontal = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Numbers,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 5.dp
                                )
                        ) {
                            Text(
                                text = "版本代码",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = AppUtils.getAppVersionCode().toString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                            .clickable {
                                onNotice()
                            }
                            .padding(
                                horizontal = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LibraryBooks,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 5.dp
                                )
                        ) {
                            Text(
                                text = "开放源代码许可",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                            .clickable {
                                customTabs(EcosedUrl.developerGithub)
                            }
                            .padding(
                                horizontal = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PeopleAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 5.dp
                                )
                        ) {
                            Text(
                                text = "开发者",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "wyq0918dev",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                            .clickable {
                                customTabs(EcosedUrl.sourcesCode)
                            }
                            .padding(
                                horizontal = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Source,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(
                            modifier = Modifier
                                .weight(
                                    weight = 1f
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 5.dp
                                )
                        ) {
                            Text(
                                text = "源代码",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
            OutlinedCard(
                onClick = {
                    customTabs(EcosedUrl.sourcesCode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 6.dp,
                        end = 12.dp,
                        bottom = 6.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "支持开发",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 4.dp
                            )
                        )
                        Text(
                            text = "LibEcosed 框架将保持免费和开源，向开发者捐赠以表示支持。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            OutlinedCard(
                onClick = {
                    customTabs(EcosedUrl.sourcesCode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 6.dp,
                        end = 12.dp,
                        bottom = 6.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "了解 LibEcosed",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(
                            modifier = Modifier.height(
                                height = 4.dp
                            )
                        )
                        Text(
                            text = "了解如何在已有工程中使用 LibEcosed 框架。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 6.dp,
                        end = 12.dp,
                        bottom = 12.dp
                    )
            ) {
                Column {
                    Text(
                        text = "Powered by LibEcosed",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@ScreenPreviews
@Composable
private fun ScreenAboutPreview() {
    LibEcosedTheme {
        ScreenAbout(
            scope = rememberCoroutineScope(),
            snackBarHostState = remember {
                SnackbarHostState()
            },
            onEasterEgg = {},
            onNotice = {},
            customTabs = {}
        )
    }
}