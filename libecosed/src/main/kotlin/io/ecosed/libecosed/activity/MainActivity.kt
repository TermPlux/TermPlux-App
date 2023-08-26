package io.ecosed.libecosed.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.DisplayFeature
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.ecosed.libecosed.R
import io.ecosed.libecosed.adapter.PagerAdapter
import io.ecosed.libecosed.plugin.LibEcosedPlugin
import io.ecosed.libecosed.ui.layout.ActivityMain
import io.ecosed.libecosed.ui.theme.LibEcosedTheme
import io.ecosed.libecosed.utils.PageTransformerUtils
import io.ecosed.libecosed.utils.ThemeHelper
import io.ecosed.plugin.PluginExecutor
import kotlinx.coroutines.Runnable
import rikka.core.res.isNight
import rikka.core.res.resolveColor
import rikka.material.app.MaterialActivity

internal class MainActivity : MaterialActivity(), Runnable {

    private var mVisible: Boolean by mutableStateOf(value = true)
    private var actionBarVisible: Boolean by mutableStateOf(value = true)

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


    private var mMainFragment: Fragment? = null
    private var mProductLogo: Drawable? = null

    private lateinit var mViewPager2: ViewPager2


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        mMainFragment = PluginExecutor.execMethodCall(
            activity = this@MainActivity,
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.getMainFragment
        ) as Fragment

        mProductLogo = PluginExecutor.execMethodCall(
            activity = this@MainActivity,
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.getProductLogo
        ) as Drawable

        mVisible = true
        title = AppUtils.getAppName()

        super.onCreate(savedInstanceState)

        val madapter = PagerAdapter(
            activity = this@MainActivity,
            mainFragment = mMainFragment
        )

        mViewPager2 = ViewPager2(this@MainActivity).apply {
            isUserInputEnabled = true
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = madapter
        }

        setContent {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
            val displayFeatures: List<DisplayFeature> =
                calculateDisplayFeatures(activity = this)
            LocalView.current.setOnTouchListener(delayHideTouchListener)
            LibEcosedTheme(
                dynamicColor = ThemeHelper.isUsingSystemColor()
            ) { dynamic ->
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    productLogo = mProductLogo,
                    topBarVisible = actionBarVisible,
                    topBarUpdate = { toolbar ->
                        setSupportActionBar(toolbar)
                    },
                    viewPager2 = mViewPager2,
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = {
                        mViewPager2.currentItem = it
                    },
                    toggle = {
                        toggle()
                    },
                    taskbar = {
                        Taskbar.openSettings(
                            this@MainActivity,
                            getString(R.string.taskbar_title),
                            when (dynamic){
                                true -> R.style.Theme_LibEcosed_TaskbarDynamic
                                false -> R.style.Theme_LibEcosed_Taskbar
                            }
                        )
                    },
                    customTabs = { url ->
                        CustomTabsIntent.Builder()
                            .build()
                            .launchUrl(
                                this@MainActivity,
                                Uri.parse(url)
                            )
                    }
                )
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        intent.categories.forEach { category ->
            if (category != Intent.CATEGORY_HOME) {
                super.onBackPressed()
            }
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar).let {
            supportActionBar?.apply {
                setDisplayShowCustomEnabled(true)
                setCustomView(
                    TabLayout(this@MainActivity).apply {
                        setBackgroundColor(Color.Transparent.toArgb())
                        tabMode = TabLayout.MODE_AUTO
                        TabLayoutMediator(this, mViewPager2) { tab, position ->
                            tab.text = when (position) {
                                0 -> "主页"
                                1 -> "桌面"
                                2 -> "设置"
                                else -> "Error"
                            }
                        }.attach()
                    },
                    ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT
                    )
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
            if (ThemeHelper.isUsingSystemColor()) {
                theme.applyStyle(R.style.ThemeOverlay_LibEcosed_DynamicColors, true)
            }
            theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@MainActivity), true)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onApplyTranslucentSystemBars() {
        super.onApplyTranslucentSystemBars()
//        val window = window
//        val theme = theme
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            window?.decorView?.post {
//                if (window.decorView.rootWindowInsets?.systemWindowInsetBottom ?: 0 >= Resources.getSystem().displayMetrics.density * 40) {
//                    window.navigationBarColor =
//                        theme.resolveColor(android.R.attr.navigationBarColor) and 0x00ffffff or -0x20000000
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        window.isNavigationBarContrastEnforced = false
//                    }
//                } else {
//                    window.navigationBarColor = Color.Transparent.toArgb()
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        window.isNavigationBarContrastEnforced = true
//                    }
//                }
//            }
//        }

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
            R.id.action_settings -> Toast.makeText(this@MainActivity, "设置", Toast.LENGTH_SHORT)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun run() {

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