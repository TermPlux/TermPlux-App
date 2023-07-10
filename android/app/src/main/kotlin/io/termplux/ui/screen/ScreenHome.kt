package io.termplux.ui.screen

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.R
import io.termplux.ui.preview.ScreenPreviews
import io.termplux.ui.widget.RootContent
import io.termplux.ui.widget.TopActionBar

@Composable
fun ScreenHome(
    topBarVisible: Boolean,
    topBarUpdate: (MaterialToolbar) -> Unit,
    rootLayout: FrameLayout
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.custom_wallpaper_24
                ),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(
                            weight = 1f,
                            fill = true
                        )
                ) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            ),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        TopActionBar(
                            modifier = Modifier.fillMaxWidth(),
                            visible = topBarVisible,
                            update = topBarUpdate
                        )
                    }
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                    ) {
                        RootContent(
                            rootLayout = rootLayout,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .clickable {

                            }
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
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Search...",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        IconButton(
                            onClick = {
                                //    toggle()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@ScreenPreviews
fun ScreenHomePreview() {
    val context = LocalContext.current
    ScreenHome(
        topBarVisible = true,
        topBarUpdate = {},
        rootLayout = FrameLayout(context).apply {
            addView(
                TextView(context).apply {
                    text = stringResource(
                        id = R.string.flutter_view_preview
                    )
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    )
}