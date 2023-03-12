package io.termplux.ui.screen

import android.view.Gravity
import android.widget.TextClock
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.termplux.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenManager(
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
    onNavigateToApps: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val expandedPowerButton = remember {
        mutableStateOf(true)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {


            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    LinearLayoutCompat(context).apply {
                        orientation = LinearLayoutCompat.VERTICAL
                        addView(
                            TextClock(context).apply {
                                format12Hour = "hh:mm"
                                format24Hour = "HH:mm"
                                textSize = 40F
                                gravity = Gravity.CENTER
                            },
                            LinearLayoutCompat.LayoutParams(
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                            )
                        )
                        addView(
                            TextClock(context).apply {
                                format12Hour = "yyyy/MM/dd E"
                                format24Hour = "yyyy/MM/dd E"
                                textSize = 16F
                                gravity = Gravity.CENTER
                            },
                            LinearLayoutCompat.LayoutParams(
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                            )
                        )
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Surface(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Surface(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Apps,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Surface(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Surface(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SelectAll,
                            contentDescription = null
                        )
                    }
                }
            }





            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 5.dp
                        )
                        .heightIn(min = 70.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.custom_termplux_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .padding(top = 5.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = targetAppName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = targetAppPackageName,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = targetAppDescription,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = targetAppVersionName,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .wrapContentWidth(Alignment.End)
                    )
                    Switch(
                        checked = expandedPowerButton.value,
                        onCheckedChange = {
                            expandedPowerButton.value = it
                        },
                        modifier = Modifier
                            .padding(all = 5.dp)
                            .wrapContentWidth(Alignment.End),
                        enabled = true
                    )
                }
            }



            ElevatedCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 5.dp
                        )
                ) {
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "启动运行环境",
                                color = Color.Green
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOn,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Green
                            )
                        }
                    )
                    AssistChip(
                        onClick = {

                        },
                        label = {
                            Text(
                                text = "停止运行环境",
                                color = Color.Red
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = expandedPowerButton.value,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ToggleOff,
                                contentDescription = null,
                                modifier = Modifier.size(AssistChipDefaults.IconSize),
                                tint = Color.Red
                            )
                        }
                    )
                }
            }

            Divider()



            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "设备信息",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                            top = 5.dp,
                            bottom = 2.5.dp
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                            top = 2.5.dp,
                            bottom = 2.5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Android,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "sb",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 5.dp,
                            )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                            top = 2.5.dp,
                            bottom = 2.5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhoneAndroid,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "sb",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 5.dp,
                            )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                            top = 2.5.dp,
                            bottom = 2.5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DesignServices,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "sb",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 5.dp,
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScreenManagerPreview() {
    ScreenManager(
        targetAppName = "TermPlux",
        targetAppPackageName = "io.termplux",
        targetAppDescription = "TermPlux Project",
        targetAppVersionName = "1.0",
        NavigationOnClick = {},
        MenuOnClick = {},
        SearchOnClick = {},
        SheetOnClick = {},
        AppsOnClick = {},
        SelectOnClick = {},
        onNavigateToApps = {}
    )
}