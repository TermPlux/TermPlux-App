package io.termplux.ui.layout

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.ui.R
import io.termplux.ui.preview.ScreenPreviews
import io.termplux.ui.theme.TermPluxAppTheme
import io.termplux.ui.widget.TopActionBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(
    topBarView: MaterialToolbar,
    container: FragmentContainerView,
    rootLayout: FrameLayout
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
//            Image(
//                painter = painterResource(
//                    id = R.drawable.custom_wallpaper_24
//                ),
//                contentDescription = null,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier.fillMaxSize()
//            )
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
                            factory = topBarView,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                    ) {
//                        RootContent(
//                            rootLayout = rootLayout,
//                            modifier = Modifier.fillMaxSize()
//                        )
                        AndroidView(
                            factory = {
                                container
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                ElevatedCard(
                    onClick = {},
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
    TermPluxAppTheme {
        ScreenHome(
            topBarView = MaterialToolbar(LocalContext.current).apply {
                title = "toolbar_preview"
                navigationIcon = ContextCompat.getDrawable(
                    LocalContext.current,
                    R.drawable.baseline_arrow_back_24
                )
            },
            container = FragmentContainerView(context),
            rootLayout = FrameLayout(context).apply {
                addView(
                    TextView(context).apply {
                        text = "flutter_view"
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
}