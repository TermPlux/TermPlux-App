package io.ecosed.libecosed.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.customview.widget.Openable
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
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

internal class MainActivity : MaterialActivity() {



    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var mContainer: FragmentContainerView

    private var mVisible: Boolean by mutableStateOf(value = true)
    private var actionBarVisible: Boolean by mutableStateOf(value = true)

    private val mFlutterFrame: FrameLayout by unsafeLazy {
        FrameLayout(this)
    }

    private val mHandler = Looper.myLooper()?.let {
        Handler(it)
    }

    private val showPart2Runnable = Runnable {
        actionBarVisible = true
    }

    private val hideRunnable = Runnable {
        hide()
    }

    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (autoHide) delayedHide()
            MotionEvent.ACTION_UP -> view.performClick()
        }
        false
    }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        title = AppUtils.getAppName(
            execMethodCall(
                activity = this@MainActivity,
                name = LibEcosed.channel,
                method = LibEcosed.getPackage
            ).toString()
        )
        super.onCreate(savedInstanceState)
        mVisible = true


        setContent {
            LocalView.current.setOnTouchListener(delayHideTouchListener)

            ContainerBinding.inflate(layoutInflater).container.let {
                navController = (supportFragmentManager.findFragmentById(
                    it.id
                ) as NavHostFragment).navController
                appBarConfiguration = AppBarConfiguration(
                    navGraph = navController.graph,
                    drawerLayout = object : Openable {
                        override fun isOpen(): Boolean {
                            return true
                        }

                        override fun open() {
                            Toast.makeText(this@MainActivity, "open", Toast.LENGTH_SHORT).show()
                        }

                        override fun close() {
                            Toast.makeText(this@MainActivity, "close", Toast.LENGTH_SHORT).show()
                        }

                    }
                )
                mContainer = it
            }

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
                    topBarVisible = actionBarVisible,
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
            context = this@MainActivity
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
                theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@MainActivity), true)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> navController.navigate(R.id.nav_settings)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        actionBarVisible = false
        mVisible = false
        mHandler?.removeCallbacks(showPart2Runnable)
    }

    private fun show() {
        mVisible = true
        mHandler?.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
    }

    private fun delayedHide() {
        mHandler?.removeCallbacks(hideRunnable)
        mHandler?.postDelayed(hideRunnable, autoHideDelayMillis.toLong())
    }

    companion object {
        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = false

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300
    }
}