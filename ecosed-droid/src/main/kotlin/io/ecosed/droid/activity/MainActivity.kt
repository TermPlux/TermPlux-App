//package io.ecosed.droid.activity
//
//import android.annotation.SuppressLint
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.content.res.Resources
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Bundle
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.MotionEvent
//import android.view.View
//import android.widget.Toast
//import androidx.activity.compose.setContent
//import androidx.appcompat.app.ActionBar
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.appcompat.widget.Toolbar
//import androidx.browser.customtabs.CustomTabsIntent
//import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
//import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalView
//import androidx.core.view.WindowCompat
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.viewpager2.widget.ViewPager2
//import com.blankj.utilcode.util.AppUtils
//import com.farmerbb.taskbar.lib.Taskbar
//import com.google.accompanist.adaptive.calculateDisplayFeatures
//import com.google.android.material.internal.EdgeToEdgeUtils
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayoutMediator
//import io.ecosed.droid.EcosedFramework
//import io.ecosed.droid.R
//import io.ecosed.droid.adapter.PagerAdapter
//import io.ecosed.droid.plugin.LibEcosedPlugin
//import io.ecosed.droid.service.EcosedService
//import io.ecosed.droid.ui.layout.ActivityMain
//import io.ecosed.droid.ui.theme.LibEcosedTheme
//import io.ecosed.droid.utils.ThemeHelper
//import io.ecosed.droid.plugin.PluginExecutor
//import kotlinx.coroutines.Runnable
//import rikka.material.app.MaterialActivity
//
//internal class MainActivity : MaterialActivity(), ServiceConnection, DefaultLifecycleObserver, Runnable {
//
//    private var mVisible: Boolean by mutableStateOf(value = true)
//    private var actionBarVisible: Boolean by mutableStateOf(value = true)
//
//    private val mHandler = Looper.myLooper()?.let {
//        Handler(it)
//    }
//
//    private val showPart2Runnable = Runnable {
//        actionBarVisible = true
//    }
//
//    private val hideRunnable = Runnable {
//        hide()
//    }
//
//    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
//        when (motionEvent.action) {
//            MotionEvent.ACTION_DOWN -> if (autoHide) delayedHide()
//            MotionEvent.ACTION_UP -> view.performClick()
//        }
//        false
//    }
//
//    private var mProductLogo: Drawable? = null
//
//    private lateinit var mViewPager2: ViewPager2
//
//    private lateinit var mActivity: MainActivity
//
//    private lateinit var mEcosed: Intent
//    private var mFramework: EcosedFramework? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//
//
//        mActivity = this@MainActivity
//
//        mViewPager2 = ViewPager2(this@MainActivity).apply {
//            isUserInputEnabled = true
//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            adapter = PagerAdapter(activity = this@MainActivity)
//            offscreenPageLimit = (adapter as PagerAdapter).itemCount
//
//
//
//            //  setPageTransformer(PageTransformerUtils())
//        }
//
//        mProductLogo = PluginExecutor.execMethodCall<Drawable>(
//            activity = mActivity,
//            name = LibEcosedPlugin.channel,
//            method = LibEcosedPlugin.getProductLogo,
//            null
//        )
//
//        mEcosed = Intent(this@MainActivity, EcosedService().javaClass)
//        startService(mEcosed)
//        bindService(mEcosed, this@MainActivity, Context.BIND_AUTO_CREATE)
//
//        mVisible = true
//        title = AppUtils.getAppName()
//
//        super<MaterialActivity>.onCreate(savedInstanceState)
//
//
//
//        lifecycle.addObserver(mActivity)
//
//
//    }
//
//    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//        Log.i(tag, "服务端绑定成功")
//
//        mFramework = EcosedFramework.Stub.asInterface(service)
//    }
//
//    override fun onServiceDisconnected(name: ComponentName?) {
//
//    }
//
//    override fun onBindingDied(name: ComponentName?) {
//        super.onBindingDied(name)
//    }
//
//    override fun onNullBinding(name: ComponentName?) {
//        super.onNullBinding(name)
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        intent.categories.forEach { category ->
//            if (category != Intent.CATEGORY_HOME) {
//                super.onBackPressed()
//            }
//        }
//    }
//
//    override fun setSupportActionBar(toolbar: Toolbar?) {
//        super.setSupportActionBar(toolbar).let {
//            supportActionBar?.apply {
//                setDisplayShowCustomEnabled(true)
//                setCustomView(
//                    TabLayout(mActivity).also {
//                        it.setBackgroundColor(Color.Transparent.toArgb())
//                        it.tabMode = TabLayout.MODE_AUTO
//                        TabLayoutMediator(it, mViewPager2) { tab, position ->
//                            tab.text = when (position) {
//                                0 -> "主页"
//                                1 -> "桌面"
//                                2 -> "设置"
//                                else -> "Error"
//                            }
//                        }.attach()
//                    },
//                    ActionBar.LayoutParams(
//                        ActionBar.LayoutParams.MATCH_PARENT,
//                        ActionBar.LayoutParams.MATCH_PARENT
//                    )
//                )
//            }
//        }
//    }
//
//    override fun computeUserThemeKey(): String {
////        return super.computeUserThemeKey().let {
////            ThemeHelper.getTheme(
////                context = this@MainActivity
////            ) + ThemeHelper.isUsingSystemColor()
////        }
//        return ThemeHelper.getTheme(
//            context = this@MainActivity
//        ) + ThemeHelper.isUsingSystemColor()
//    }
//
//    override fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {
//        super.onApplyUserThemeResource(
//            theme = theme,
//            isDecorView = isDecorView
//        ).run {
//            if (ThemeHelper.isUsingSystemColor()) {
//                theme.applyStyle(R.style.ThemeOverlay_LibEcosed_DynamicColors, true)
//            }
//            theme.applyStyle(ThemeHelper.getThemeStyleRes(context = this@MainActivity), true)
//        }
//    }
//
//    @SuppressLint("RestrictedApi")
//    override fun onApplyTranslucentSystemBars() {
//        super.onApplyTranslucentSystemBars()
//        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> Toast.makeText(this@MainActivity, "设置", Toast.LENGTH_SHORT)
//                .show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
//    override fun onCreate(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onCreate(owner)
//        setContent {
//            LocalView.current.setOnTouchListener(delayHideTouchListener)
//            LibEcosedTheme(
//                dynamicColor = ThemeHelper.isUsingSystemColor()
//            ) { dynamic ->
//                ActivityMain(
//                    windowSize = calculateWindowSizeClass(
//                        activity = this@MainActivity
//                    ),
//                    displayFeatures = calculateDisplayFeatures(
//                        activity = this@MainActivity
//                    ),
//                    productLogo = mProductLogo,
//                    topBarVisible = actionBarVisible,
//                    topBarUpdate = { toolbar ->
//                        setSupportActionBar(
//                            toolbar = toolbar
//                        )
//                    },
//                    viewPager2 = mViewPager2,
//                    androidVersion = "13",
//                    shizukuVersion = "13",
//                    current = {
//                        mViewPager2.currentItem = it
//                    },
//                    toggle = {
//                        toggle()
//                    },
//                    taskbar = {
//                        Taskbar.openSettings(
//                            this@MainActivity,
//                            getString(R.string.taskbar_title),
//                            when (dynamic){
//                                true -> R.style.Theme_LibEcosed_TaskbarDynamic
//                                false -> R.style.Theme_LibEcosed_Taskbar
//                            }
//                        )
//                    },
//                    launchUrl = { url ->
//                        CustomTabsIntent.Builder()
//                            .build()
//                            .launchUrl(
//                                this@MainActivity,
//                                Uri.parse(url)
//                            )
//                    }
//                )
//            }
//        }
//    }
//
//    override fun onStart(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onStart(owner)
//    }
//
//    override fun onResume(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onResume(owner)
//    }
//
//    override fun onPause(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onPause(owner)
//    }
//
//    override fun onStop(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onStop(owner)
//    }
//
//    override fun onDestroy(owner: LifecycleOwner) {
//        super<DefaultLifecycleObserver>.onDestroy(owner)
//    }
//
//    override fun run() {
//
//    }
//
//    private fun toggle() {
//        if (mVisible) {
//            hide()
//        } else {
//            show()
//        }
//    }
//
//    private fun hide() {
//        actionBarVisible = false
//        mVisible = false
//        mHandler?.removeCallbacks(showPart2Runnable)
//    }
//
//    private fun show() {
//        mVisible = true
//        mHandler?.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
//    }
//
//    private fun delayedHide() {
//        mHandler?.removeCallbacks(hideRunnable)
//        mHandler?.postDelayed(hideRunnable, autoHideDelayMillis.toLong())
//    }
//
//    companion object {
//
//        const val tag: String = "MainActivity"
//
//        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
//        const val autoHide = false
//
//        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
//        const val autoHideDelayMillis = 3000
//
//        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
//        const val uiAnimatorDelay = 300
//
//
//    }
//
//
//}