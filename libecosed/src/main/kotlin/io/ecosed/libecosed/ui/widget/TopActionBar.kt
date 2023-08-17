package io.ecosed.libecosed.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import io.ecosed.libecosed.R
import io.ecosed.libecosed.ui.preview.WidgetPreview
import io.ecosed.libecosed.ui.theme.LibEcosedTheme

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun TopActionBar(
    navController: NavController,
    configuration: AppBarConfiguration,
    modifier: Modifier,
    visible: Boolean,
    update: (MaterialToolbar) -> Unit
) {
    val toolbarParams: AppBarLayout.LayoutParams = AppBarLayout.LayoutParams(
        AppBarLayout.LayoutParams.MATCH_PARENT,
        AppBarLayout.LayoutParams.WRAP_CONTENT
    )

    val toolbar: MaterialToolbar = MaterialToolbar(
        LocalContext.current
    ).apply {
        setBackgroundColor(
            Color.Transparent.toArgb()
        )
        update(this@apply)
        setupWithNavController(
            navController = navController,
            configuration = configuration
        )
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        label = ""
    ) {
        AndroidView(
            factory = { context ->
                AppBarLayout(context).apply {
                    addView(toolbar, toolbarParams)
                }
            },
            onReset = { appBar ->
                appBar.removeView(toolbar)
                appBar.addView(toolbar, toolbarParams)
            },
            modifier = modifier,
            update = { appBar ->
                appBar.setBackgroundColor(Color.Transparent.value.toInt())
                appBar.isLiftOnScroll = true
                appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                    appBar.context
                )
            },
            onRelease = { appBar ->
                appBar.removeView(toolbar)
            }
        )
    }
}

@Composable
@WidgetPreview
fun TopActionBarPreview() {
    val title = stringResource(
        id = R.string.toolbar_preview
    )
    val icon = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.baseline_arrow_back_24
    )
    LibEcosedTheme {
        TopActionBar(
            navController = rememberNavController(),
            configuration = AppBarConfiguration.Builder().build(),
            modifier = Modifier.fillMaxWidth(),
            visible = true
        ) {
            it.title = title
            it.navigationIcon = icon
        }
    }
}