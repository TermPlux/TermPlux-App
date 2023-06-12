package io.termplux.activity

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.*
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
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
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.BuildConfig
import io.termplux.IUserService
import io.termplux.R
import io.termplux.adapter.AppsAdapter
import io.termplux.custom.DisableSwipeViewPager
import io.termplux.custom.LinkNativeViewFactory
import io.termplux.fragment.ContainerFragment
import io.termplux.fragment.FlutterFragment
import io.termplux.model.AppsModel
import io.termplux.receiver.AppsReceiver
import io.termplux.services.MainService
import io.termplux.services.UserService
import io.termplux.ui.ActivityMain
import io.termplux.utils.FlutterViewReturn
import io.termplux.utils.LifeCircleUtils
import rikka.shizuku.Shizuku
import java.lang.ref.WeakReference
import kotlin.math.hypot

@SuppressLint(value = ["NonConstantResourceId"])
@DarkStatusBarTheme(value = true)
@DarkNavigationBarTheme(value = true)
@NavigationBarBackgroundColorRes(value = R.color.transparent)
@FragmentLayout(value = R.id.fragment_container)
class MainActivity : BaseActivity(), FlutterBoostDelegate, FlutterPlugin, FlutterViewReturn,
    MethodChannel.MethodCallHandler, FlutterEngineConfigurator, Shizuku.OnBinderReceivedListener,
    Shizuku.OnBinderDeadListener, Shizuku.OnRequestPermissionResultListener,
    DefaultLifecycleObserver, Runnable {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

    // 平台通道
    private lateinit var channel: MethodChannel

    private lateinit var mSharedPreferences: SharedPreferences

    // Shizuku用户服务
    private lateinit var userServices: IUserService


    private lateinit var mMainFragment: FlutterFragment

    private lateinit var mRootLayout: FrameLayout
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mFlutterView: FlutterView
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mMaterialToolbar: MaterialToolbar
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var appReceiver: AppsReceiver

    private var isDynamicColor: Boolean by mutableStateOf(value = true)

    private val mHandler = Looper.myLooper()?.let {
        Handler(it)
    }

    private val showPart2Runnable = Runnable {
        supportActionBar?.show()
        //systemUiVisible = true
    }
    private var mVisible: Boolean = false

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


    override fun resetContentView(): DisableSwipeViewPager = DisableSwipeViewPager(
        context = mContext
    ).apply {
        id = R.id.fragment_container
    }

    @SuppressLint("RestrictedApi")
    override fun initViews() {
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
                        FlutterFragment::class.java
                    )
                        .destroyEngineWithFragment(false)
                        .renderMode(RenderMode.surface)
                        .transparencyMode(TransparencyMode.opaque)
                        .shouldAttachEngineToActivity(true)
                        .build<FlutterFragment>()?.also {
                            mMainFragment = it
                        }
                }
            }
        }

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
                create = {
                    // 注册监听器
                    Shizuku.addBinderReceivedListenerSticky(this@MainActivity)
                    Shizuku.addBinderDeadListener(this@MainActivity)
                    Shizuku.addRequestPermissionResultListener(this@MainActivity)
                    // 初始化用户服务
                    initServices()
                },
                destroy = {
                    // 注销监听
                    Shizuku.removeBinderReceivedListener(this@MainActivity)
                    Shizuku.removeBinderDeadListener(this@MainActivity)
                    Shizuku.removeRequestPermissionResultListener(this@MainActivity)
                    // 解绑用户服务
                    Shizuku.unbindUserService(mUserServiceArgs, mUserServiceConnection, true)
                    // 移除生命周期
                    lifecycle.removeObserver(this@MainActivity)
                    unbindService(mConnection)
                }
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

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun returnFlutterView(flutterView: FlutterView?) {
        // 获取FlutterView
        mFlutterView = flutterView ?: errorFlutterViewIsNull()
        // 移除FlutterView
        (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
        // 添加生命周期
        lifecycle.addObserver(this@MainActivity)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        // 初始化平台通道
        val messenger = flutterEngine.dartExecutor.binaryMessenger
        channel = MethodChannel(messenger, channelName)
    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun onBinderReceived() {

    }

    override fun onBinderDead() {

    }

    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
        if (
            grantResult == PackageManager.PERMISSION_GRANTED
        ) {

        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
        // 更新操作栏状态
        mVisible = true
        // 首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        // 获取动态颜色首选项
        isDynamicColor = mSharedPreferences.getBoolean(
            "dynamic_colors",
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


        // 检查权限
        check()
        // 初始化UI控件
        initUiView()


        // 加载应用列表
        loadApp()
        // 用于刷新应用列表的广播接收器
        AppsReceiver {
            loadApp()
        }.also {
            // 注册广播接收器
            registerReceiver(
                it,
                IntentFilter().apply {
                    addAction(Intent.ACTION_PACKAGE_ADDED)
                    addAction(Intent.ACTION_PACKAGE_REMOVED)
                    addDataScheme("package")
                }
            )
            appReceiver = it
        }

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setIcon(R.drawable.baseline_terminal_24)

        if (false) setContentView(mRootLayout) else setContent {
            channel.setMethodCallHandler { call, res ->
                when (call.method) {
                    "pager" -> {
                        // mViewPager2.currentItem = PagerAdapter.compose
                        res.success("success")

                    }

                    "dynamic_colors" -> res.success(isDynamicColor)
                    "toggle" -> toggle()

                    else -> {
                        res.error("error", "error_message", null)
                    }
                }
            }
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            )
            val windowSize = calculateWindowSizeClass(
                activity = mME
            )
            val displayFeatures = calculateDisplayFeatures(
                activity = mME
            )

            TermPluxTheme(dynamicColor = isDynamicColor) {
                ActivityMain(
                    navController = navController,
                    drawerState = drawerState,
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    topBar = {
                        AndroidView(
                            factory = {
                                mAppBarLayout
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    pager = {
                        AndroidView(
                            factory = {
                                mRootLayout
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    navBar = {},
                    tabRow = {},
                    optionsMenu = {
                        optionsMenu()
                    },
                    androidVersion = getAndroidVersion(),
                    shizukuVersion = getShizukuVersion(),
                    current = {},
                    toggle = {
                        toggle()
                    }
                )

            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
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
        mFlutterView.removeCallbacks(this@MainActivity)
        unregisterReceiver(appReceiver)
    }


    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mFlutterView.width.toFloat(),
            y = mFlutterView.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mFlutterView,
                cx.toInt(),
                cy.toInt(),
                startRadius,
                endRadius
            )
            .setDuration(
                splashPart2AnimatorMillis.toLong()
            )
        mSplashLogo.animate()
            .alpha(0f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(
                splashPart1AnimatorMillis.toLong()
            )
            .withEndAction {
                mSplashLogo.visibility = View.GONE
            }
            .withStartAction {
                mFlutterView.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    private fun initUiView() {
        AppBarLayout(
            mContext
        ).apply {
            fitsSystemWindows = true
            addView(
                MaterialToolbar(
                    context
                ).also { toolbar ->
                    setSupportActionBar(toolbar)
                    mMaterialToolbar = toolbar
                },
                AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT,
                    AppBarLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }.also { appBar ->
            mAppBarLayout = appBar
            appBar.isLiftOnScroll = true
            appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                mContext
            )
        }

        FrameLayout(
            mContext
        ).apply {
            addView(
                mFlutterView.apply {
                    setOnTouchListener(delayHideTouchListener)
                    visibility = View.INVISIBLE
                    post(this@MainActivity)
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                AppCompatImageView(context).apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            mContext,
                            R.drawable.custom_termplux_24
                        )
                    )
                }.also { logo ->
                    mSplashLogo = logo
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }.also { root ->
            mRootLayout = root
        }

        RecyclerView(mContext).apply {
            layoutManager = GridLayoutManager(
                context,
                4,
                RecyclerView.VERTICAL,
                false
            )
        }.also { apps ->
            mRecyclerView = apps
        }
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        // systemUiVisible = false
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


    private fun errorFlutterViewIsNull(): Nothing {
        error("操你妈的这逼FlutterView是空的啊啊啊啊啊")
    }

    private val mUserServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, binder: IBinder?) {
            if (binder != null && binder.pingBinder()) {
                userServices = IUserService.Stub.asInterface(binder)
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {

        }
    }

    private val mUserServiceArgs = Shizuku.UserServiceArgs(
        ComponentName(
            BuildConfig.APPLICATION_ID,
            UserService().javaClass.name
        )
    )
        .daemon(false)
        .processNameSuffix("service")
        .debuggable(BuildConfig.DEBUG)
        .version(BuildConfig.VERSION_CODE)

    private fun check() {
        try {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0)
            }
        } catch (e: Exception) {
            if (e.javaClass == IllegalStateException().javaClass) {
                PopTip.show("Shizuku未激活")
            }
        }
    }

    private fun initServices() {
        val intent = Intent(mContext, MainService().javaClass)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    // 服务绑定的监听器
    private val mConnection: ServiceConnection = object : ServiceConnection {
        // 后台服务绑定成功后执行
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            PopTip.show("服务绑定成功")
        }


        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }

    private fun getAndroidVersion(): String {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.N -> "Android Nougat 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat 7.1"
            Build.VERSION_CODES.O -> "Android Oreo 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo 8.1"
            Build.VERSION_CODES.P -> "Android Pie 9.0"
            Build.VERSION_CODES.Q -> "Android Queen Cake 10.0"
            Build.VERSION_CODES.R -> "Android Red Velvet Cake 11.0"
            Build.VERSION_CODES.S -> "Android Snow Cone 12.0"
            Build.VERSION_CODES.S_V2 -> "Android Snow Cone V2 12.1"
            Build.VERSION_CODES.TIRAMISU -> "Android Tiramisu 13.0"
            34 -> "Android Upside Down Cake 14.0"
            else -> "unknown"
        }
    }

    private fun getShizukuVersion(): String {
        try {
            val ver: String = Shizuku.getVersion().toString()
            return "Shizuku $ver"
        } catch (e: Exception) {

        }

        return ""
    }

    private fun optionsMenu() {
        if (mVisible) mMaterialToolbar.showOverflowMenu() else {
            show()
            mMaterialToolbar.hideOverflowMenu()
        }
    }

    // 打开任务栏设置
    private fun taskbar() {
        Taskbar.openSettings(
            mContext,
            getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }

    @Suppress("DEPRECATION")
    private fun loadApp() {
        // 应用列表
        val applicationList: MutableList<AppsModel> = ArrayList()

        // 添加自己
        applicationList.add(
            AppsModel(pkgName = BuildConfig.APPLICATION_ID)
        )

        // 获取启动器列表
        for (resolveInfo in if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) packageManager.queryIntentActivities(
            Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
        ) else packageManager.queryIntentActivities(
            Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            PackageManager.MATCH_ALL
        )) {
            val pkg = resolveInfo.activityInfo.packageName
            if (pkg != BuildConfig.APPLICATION_ID) {
                applicationList.add(
                    AppsModel(pkgName = pkg)
                )
            }
        }

        // 设置适配器
        mRecyclerView.apply {
            adapter = AppsAdapter.newInstance(
                applicationList = applicationList,
                current = {

                }
            )
        }
    }


    companion object {

        const val channelName: String = "termplux_channel"

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = false

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300

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
            dynamicColor: Boolean,
            content: @Composable () -> Unit
        ) {
            val view = LocalView.current
            val window = (view.context as BaseActivity).window
            val systemUiController = rememberSystemUiController()


            val colorScheme = when {
                dynamicColor -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }

            if (!view.isInEditMode) {
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme
                    )
                    WindowCompat.getInsetsController(
                        window,
                        view
                    ).isAppearanceLightStatusBars = !darkTheme
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