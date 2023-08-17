package io.ecosed.libecosed.activity

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.window.layout.DisplayFeature
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.internal.EdgeToEdgeUtils
import io.ecosed.libecosed.R
import io.ecosed.libecosed.databinding.ContainerBinding
import io.ecosed.libecosed.plugin.LibEcosed
import io.ecosed.libecosed.ui.layout.ActivityMain
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.utils.ThemeHelper
import io.ecosed.plugin.execMethodCall
import rikka.core.ktx.unsafeLazy
import rikka.core.res.isNight
import rikka.material.app.MaterialActivity

internal class ManagerActivity : MaterialActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mContainer: FragmentContainerView

    private val mFlutterFrame: FrameLayout by unsafeLazy {
        FrameLayout(this)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        title = AppUtils.getAppName(
            execMethodCall(
                activity = this@ManagerActivity,
                name = LibEcosed.channel,
                method = LibEcosed.getPackage
            ).toString()
        )
        super.onCreate(savedInstanceState)

        ContainerBinding.inflate(layoutInflater).container.let {
            navController = (supportFragmentManager.findFragmentById(
                it.id
            ) as NavHostFragment).navController
            appBarConfiguration = AppBarConfiguration(
                navGraph = navController.graph
            )
            mContainer = it
        }

        setContent {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
            val displayFeatures: List<DisplayFeature> =
                calculateDisplayFeatures(activity = this)
            LibEcosedTheme {
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    subNavController = navController,
                    configuration = appBarConfiguration,
                    container = mContainer,
                    flutter = mFlutterFrame,

                    appsUpdate = {},
                    topBarVisible = true,
                    topBarUpdate = {
                        setSupportActionBar(it)
                    },
                    preferenceUpdate = { preference ->

                    },
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = {},
                    toggle = {},
                    taskbar = {}
                )
            }
        }
    }

    override fun computeUserThemeKey(): String {
        super.computeUserThemeKey()
        return ThemeHelper.getTheme(
            context = this@ManagerActivity
        ) + ThemeHelper.isUsingSystemColor()
    }

    override fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {
        super.onApplyUserThemeResource(
            theme = theme,
            isDecorView = isDecorView
        ).run {
            if (ThemeHelper.isUsingSystemColor()) if (resources.configuration.isNight()) {
                theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Dark, true)
            } else {
                theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Light, true)
            }.run {
                theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@ManagerActivity), true)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Greeting() {
        Scaffold { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)) {
                Button(
                    onClick = {
                        execMethodCall(
                            activity = this@ManagerActivity,
                            name = LibEcosed.channel,
                            method = LibEcosed.launchApp
                        )
                    }
                ) {
                    Text(text = "Launch App")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun GreetingPreview() {
        LibEcosedTheme {
            Greeting()
        }
    }
}