package io.termplux.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.os.Build
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceManager
import com.google.android.material.internal.EdgeToEdgeUtils
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme
import com.kongzue.baseframework.interfaces.FragmentLayout
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes
import com.kongzue.baseframework.util.FragmentChangeUtil
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.WaitDialog
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.R
import io.termplux.custom.DisableSwipeViewPager
import io.termplux.custom.LinkNativeViewFactory
import io.termplux.fragment.ContainerFragment
import io.termplux.fragment.MainFragment
import io.termplux.utils.FlutterViewReturn
import io.termplux.utils.LifeCircleUtils
import java.lang.ref.WeakReference

@SuppressLint(value = ["NonConstantResourceId"])
@DarkStatusBarTheme(value = true)
@DarkNavigationBarTheme(value = true)
@NavigationBarBackgroundColorRes(value = R.color.transparent)
@FragmentLayout(value = R.id.fragment_container)
class MainActivity : BaseActivity(), FlutterBoostDelegate, FlutterPlugin, FlutterViewReturn,
    MethodCallHandler, FlutterEngineConfigurator, DefaultLifecycleObserver, Runnable {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

    private lateinit var mSharedPreferences: SharedPreferences

    private lateinit var mMainFragment: MainFragment

    private lateinit var mFlutterView: FlutterView

    override fun resetContentView(): DisableSwipeViewPager = DisableSwipeViewPager(
        context = mContext
    ).apply {
        id = R.id.fragment_container
    }

    @SuppressLint("RestrictedApi")
    override fun initViews() {
        WaitDialog.show("正在加载...")
        WeakReference(application).get()?.let { context ->
            // 初始化FlutterBoost
            FlutterBoost.instance().setup(
                context,
                this@MainActivity
            ) { engine: FlutterEngine? ->
                engine?.plugins?.add(this@MainActivity)?.let {
                    GeneratedPluginRegistrant.registerWith(engine)
                    val registry = engine.platformViewsController.registry
                    registry.registerViewFactory("android_view", LinkNativeViewFactory())
                }.also {
                    FlutterBoostFragment.CachedEngineFragmentBuilder(
                        MainFragment::class.java
                    )
                        .destroyEngineWithFragment(false)
                        .renderMode(RenderMode.surface)
                        .transparencyMode(TransparencyMode.opaque)
                        .shouldAttachEngineToActivity(true)
                        .build<MainFragment>()?.also {
                            mMainFragment = it
                        }
                }
            }
        }
        // 首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun initFragment(fragmentChangeUtil: FragmentChangeUtil?) {
        super.initFragment(fragmentChangeUtil)
        fragmentChangeUtil?.addFragment(
            ContainerFragment.newInstance(
                mainFragment = mMainFragment
            ),
            true
        )
    }

    override fun initDatas(parameter: JumpParameter?) {

    }

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            LifeCircleUtils(
                baseActivity = mME,
                observer = this@MainActivity
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> onBack()
            R.id.action_settings -> {
                PopTip.show("666")
            }
        }
        return true
    }

    override fun onPostResume() {
        super.onPostResume()
        mMainFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mMainFragment.onNewIntent(intent)
    }

    override fun onBack(): Boolean {
        super.onBack()
        mMainFragment.onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mMainFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        mMainFragment.onUserLeaveHint()
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        mMainFragment.onTrimMemory(level)
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
            MainFlutter::class.java
        )
            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
            .destroyEngineWithActivity(false)
            .uniqueId(options?.uniqueId())
            .url(options?.pageName())
            .urlParams(options?.arguments())
            .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {

    }

    override fun returnFlutterView(flutterView: FlutterView?) {
        // 获取FlutterView
        mFlutterView = flutterView ?: errorFlutterViewIsNull()
        // 移除FlutterView
        (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
        // 执行生命周期函数
        lifecycle.addObserver(this@MainActivity)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
//        setContent {
//            TermPluxTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = {}
//                ) { innerPadding: PaddingValues ->
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues = innerPadding),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        AndroidView(
//                            factory = { mFlutterView },
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                }
//            }
//        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)

        WaitDialog.dismiss()
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }


    override fun run() {

    }


    private fun errorFlutterViewIsNull(): Nothing {
        error("操你妈的这逼FlutterView是空的啊啊啊啊啊")
    }

    companion object {

        private val Purple80 = Color(0xFFD0BCFF)
        private val PurpleGrey80 = Color(0xFFCCC2DC)
        private val Pink80 = Color(0xFFEFB8C8)

        private val Purple40 = Color(0xFF6650a4)
        private val PurpleGrey40 = Color(0xFF625b71)
        private val Pink40 = Color(0xFF7D5260)

        private val Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )

        private val DarkColorScheme = darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        private val LightColorScheme = lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )

        @Composable
        fun TermPluxTheme(
            darkTheme: Boolean = isSystemInDarkTheme(),
            dynamicColor: Boolean = true,
            content: @Composable () -> Unit
        ) {
            val colorScheme = when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = colorScheme.primary.toArgb()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                        darkTheme
                }
            }

            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content
            )
        }
    }
}