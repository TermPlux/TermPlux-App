package io.ecosed.libecosed.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.appbar.MaterialToolbar
import io.ecosed.libecosed.ui.preview.ScreenPreviews
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.ui.widget.NavHostFactory
import io.ecosed.libecosed.ui.widget.TopActionBar

@Composable
internal fun ScreenContainer(
    subNavController: NavController,
    configuration: AppBarConfiguration,
    topBarVisible: Boolean,
    topBarUpdate: (MaterialToolbar) -> Unit,
    container: FragmentContainerView,
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 6.dp
                        ),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    TopActionBar(
                        navController = subNavController,
                        configuration = configuration,
                        modifier = Modifier.fillMaxWidth(),
                        visible = topBarVisible,
                        update = topBarUpdate
                    )
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 6.dp,
                            bottom = 12.dp
                        )
                ) {
                    NavHostFactory(
                        factory = container,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
@ScreenPreviews
internal fun ScreenContainerPreview() {
    LibEcosedTheme {
        ScreenContainer(
            subNavController = rememberNavController(),
            configuration = AppBarConfiguration.Builder().build(),
            topBarVisible = true,
            topBarUpdate = {

            },
            container = FragmentContainerView(
                context = LocalContext.current
            )
        )
    }
}