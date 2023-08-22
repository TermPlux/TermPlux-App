package io.ecosed.libecosed.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
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
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.DisplayFeature
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.appbar.MaterialToolbar
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
import rikka.core.res.isNight
import rikka.material.app.MaterialActivity


internal class ManagerActivity : MaterialActivity() {

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


    private lateinit var mActivity: Activity

    private lateinit var mViewPager2: ViewPager2
    private lateinit var mMaterialToolbar: MaterialToolbar


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        mActivity = PluginExecutor.execMethodCall(
            activity = this@ManagerActivity,
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.getLaunchActivity
        ) as Activity

        mVisible = true
        title = AppUtils.getAppName()

        super.onCreate(savedInstanceState)

        val madapter = PagerAdapter(
            activity = this@ManagerActivity,
            mainFragment = null,
            settingsFragment = null
        )



//        mMaterialToolbar = MaterialToolbar(this@ManagerActivity).apply {
//
//
//        }
        mViewPager2 = ViewPager2(this@ManagerActivity).apply {

            isUserInputEnabled = true
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setPageTransformer(PageTransformerUtils())
            adapter = madapter


        }

 //       this@ManagerActivity.setSupportActionBar(mMaterialToolbar)




        setContent {
            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
            val displayFeatures: List<DisplayFeature> =
                calculateDisplayFeatures(activity = this)
            LocalView.current.setOnTouchListener(delayHideTouchListener)
            LibEcosedTheme {
                ActivityMain(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    topBarVisible = actionBarVisible,
                    topBarUpdate = {
                                   this.setSupportActionBar(it)
                    },

                    viewPager2 = mViewPager2,
                    preferenceUpdate = { preference ->

                    },
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = {},
                    toggle = {
                        toggle()
                    },
                    taskbar = {}
                )
            }
        }

    }

//    private fun isLauncherEnabled(): Boolean {
//        try {
//            val component = ComponentName(this, this.javaClass)
//            return this.packageManager.getComponentEnabledSetting(component) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED
//        } catch (ignored: Exception) {
//        }
//        return false
//    }

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
                    TabLayout(this@ManagerActivity).apply {
                        setBackgroundColor(Color.Transparent.toArgb())
                        tabMode = TabLayout.MODE_AUTO
                        TabLayoutMediator(this, mViewPager2) { tab, position ->
                            tab.text = when (position) {
                                0 -> "主页"
                                1 -> "设置"
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> Toast.makeText(this@ManagerActivity, "设置", Toast.LENGTH_SHORT)
                .show()
        }
        return super.onOptionsItemSelected(item)
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