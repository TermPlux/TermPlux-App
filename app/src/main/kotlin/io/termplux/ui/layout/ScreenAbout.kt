package io.termplux.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.AppUtils
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.ui.preview.ScreenPreviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenAbout(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onEasterEgg: () -> Unit,
    onNotice: () -> Unit,
    onSource: () -> Unit,
    onDevGitHub: () -> Unit,
    onDevTwitter: () -> Unit,
    onTeamGitHub: () -> Unit
) {
    val scrollState = rememberScrollState()
    val position = remember {
        mutableStateOf(value = 0)
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
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        top = 5.dp,
                        bottom = 8.dp
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
                            painter = painterResource(
                                id = R.drawable.custom_termplux_24
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
                                text = stringResource(
                                    id = R.string.app_description
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                    ) {
                        Row(
                            modifier = Modifier
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
                                    text = BuildConfig.VERSION_NAME,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                    ) {
                        Row(
                            modifier = Modifier
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
                                    text = BuildConfig.VERSION_CODE.toString(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                    ) {
                        Row(
                            modifier = Modifier
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
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    scope.launch {
                                        scrollState.animateScrollBy(10000f)
                                    }
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
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 50.dp
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onSource()
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
            }
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp
                    )
            ) {
                Column {
                    Text(
                        text = "开发者",
                        modifier = Modifier.padding(
                            all = 16.dp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 80.dp
                            )
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 24.dp
                                    )
                                    .padding(
                                        top = 5.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = R.drawable.custom_developer_24
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(
                                            width = 48.dp,
                                            height = 48.dp
                                        )
                                        .clip(
                                            CircleShape
                                        )
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
                                        text = "wyq0918dev",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "主要开发者",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 24.dp
                                )
                            ) {
                                TextButton(
                                    onClick = {
                                        onDevGitHub()
                                    }
                                ) {
                                    Text(
                                        text = "GitHub"
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        onDevTwitter()
                                    }
                                ) {
                                    Text(
                                        text = "Twitter"
                                    )
                                }
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 80.dp
                            )
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 24.dp
                                    )
                                    .padding(
                                        top = 5.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = R.drawable.custom_termplux_24
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(
                                        width = 48.dp,
                                        height = 48.dp
                                    )
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
                                        text = "TermPlux Project Team",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "项目团队",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 24.dp
                                )
                            ) {
                                TextButton(
                                    onClick = {
                                        onTeamGitHub()
                                    }
                                ) {
                                    Text(
                                        text = "GitHub"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ScreenPreviews
@Composable
fun ScreenAboutPreview() {
    ScreenAbout(
        scope = rememberCoroutineScope(),
        snackBarHostState = remember {
            SnackbarHostState()
        },
        onEasterEgg = {},
        onNotice = {},
        onSource = {},
        onDevGitHub = {},
        onDevTwitter = {},
        onTeamGitHub = {}
    )
}