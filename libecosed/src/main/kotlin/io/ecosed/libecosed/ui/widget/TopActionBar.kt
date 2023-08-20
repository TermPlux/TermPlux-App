package io.ecosed.libecosed.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.customview.widget.Openable
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
import kotlinx.coroutines.launch

@Composable
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
internal fun TopActionBar(
    navController: NavController,
    modifier: Modifier,
    visible: Boolean,
    drawerState: DrawerState,
    update: (MaterialToolbar) -> Unit
) {
    val scope = rememberCoroutineScope()
    val toolbarParams: AppBarLayout.LayoutParams = AppBarLayout.LayoutParams(
        AppBarLayout.LayoutParams.MATCH_PARENT,
        AppBarLayout.LayoutParams.WRAP_CONTENT
    )
    val openable: Openable = object : Openable {
        override fun isOpen(): Boolean = false
        override fun close() = Unit
        override fun open() {
            scope.launch {
                drawerState.open()
            }
        }
    }
    val configuration = AppBarConfiguration(
        navGraph = navController.graph,
        drawerLayout = openable
    )
    val toolbar: MaterialToolbar = MaterialToolbar(
        LocalContext.current
    ).also { toolbar ->
        update(toolbar)
    }.apply {
        setupWithNavController(
            navController = navController,
            configuration = configuration
        )
        setBackgroundColor(
            Color.Transparent.toArgb()
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
@OptIn(ExperimentalMaterial3Api::class)
private fun TopActionBarPreview() {
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
            modifier = Modifier.fillMaxWidth(),
            visible = true,
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            )
        ) {
            it.title = title
            it.navigationIcon = icon
        }
    }
}