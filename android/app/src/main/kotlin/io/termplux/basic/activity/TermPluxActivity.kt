package io.termplux.basic.activity

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.WindowInsets
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.dialogs.PopTip
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.BuildConfig
import io.termplux.IUserService
import io.termplux.R
import io.termplux.basic.adapter.ContentAdapter
import io.termplux.basic.custom.LinkNativeViewFactory
import io.termplux.basic.services.MainService
import io.termplux.basic.services.UserService
import io.termplux.basic.utils.BackInvokedCallbackUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

abstract class TermPluxActivity : BaseActivity(), FlutterEngineConfigurator {

    private val mME: BaseActivity = me
    private val mBaseContext: Context = mME

    private val hideHandler = Handler(
        Looper.myLooper()!!
    )


    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        // shizuku进程已激活

    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        // shizuku进程被停止
    }

    //shizuku监听授权结果
    private val requestPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { requestCode: Int, grantResult: Int ->
            onRequestPermissionsResults(requestCode, grantResult)
        }

    private lateinit var onBackInvokedCallback: OnBackInvokedCallback

    private lateinit var userServices: IUserService
    private lateinit var mSharedPreferences: SharedPreferences

    private var isNeedInterceptBackEvent: Boolean by mutableStateOf(value = true)
    private var isHome: Boolean by mutableStateOf(value = false)
    private var mVisible: Boolean by mutableStateOf(value = false)

    //private var isFull: Boolean by mutableStateOf(value = true)
    private var isDynamicColor: Boolean by mutableStateOf(value = true)

    //private lateinit var mContentView: ContentView


    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mComposeView: ComposeView
    private lateinit var mViewPager2: ViewPager2

    private lateinit var mTabLayout: TabLayout

    private lateinit var mFlutterFragment: FlutterFragment


    @Suppress("DEPRECATION")
    private val hidePart2Runnable = Runnable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mComposeView.windowInsetsController?.hide(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
        } else {
            mComposeView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private val showPart2Runnable = Runnable {
        // 延迟显示UI元素
        supportActionBar?.show()
    }

    private val hideRunnable = Runnable {
        hide()
    }

    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (autoHide) delayedHide(autoHideDelayMillis)
            MotionEvent.ACTION_UP -> view.performClick()
        }
        false
    }

    override fun resetContentView(): View {
        super.resetContentView()
        // 加载ComposeView
        mComposeView = ComposeView(
            context = mBaseContext
        ).apply {
            setOnTouchListener(delayHideTouchListener)
            setViewCompositionStrategy(
                strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        return mComposeView
    }

    /**
     * initViews会在启动时首先执行，建议在此方法内进行布局绑定、View初始化等操作
     */
    @SuppressLint("RestrictedApi")
    override fun initViews() {
        mVisible = true

        initUi()
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initSP()
        initServices()
        initFlutter()
        initSystemBar()
    }

    /**
     * initData's会在布局加载后执行，建议在此方法内加载数据和处理布局显示数据
     *
     * [parameter] 从其他界面传入的数据，提供GET、SET方法获取这些数据
     */
    override fun initDatas(parameter: JumpParameter?) {

        check()
        // 子类入口函数调用
        onCreated(parameter = parameter)
    }

    /**
     * setEvents会在数据加载后执行，建议在此方法内绑定设置监听器、设置执行回调事件等操作
     */

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            object : LifeCircleListener() {

                override fun onCreate() {
                    super.onCreate()
                    // Android 13 及以上版本的返回监听
                    // 调用BaseActivity提供的onBack方法
                    if (isNeedInterceptBackEvent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        onBackInvokedCallback = BackInvokedCallbackUtils(
                            baseActivity = mME,
                            backEvent = {
                                onBack()
                            }
                        ).also {
                            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                                it
                            )
                        }
                    }
                    // 注册监听器
                    Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
                    Shizuku.addBinderDeadListener(binderDeadListener)
                    Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
                }

                override fun onResume() {
                    super.onResume()
                }

                override fun onPause() {
                    super.onPause()
                }

                override fun onDestroy() {
                    super.onDestroy()
                    // Android 13 及以上适配返回键事件，注销监听
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        onBackInvokedCallback.let {
                            onBackInvokedDispatcher.unregisterOnBackInvokedCallback(it)
                        }
                    }
                    // 注销监听
                    Shizuku.removeBinderReceivedListener(binderReceivedListener)
                    Shizuku.removeBinderDeadListener(binderDeadListener)
                    Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
                    // 解绑用户服务
                    Shizuku.unbindUserService(mUserServiceArgs, mUserServiceConnection, true)
                    unbindService(mConnection)
                }
            }
        )
        // 应用管理器
        AppManager.setOnActivityStatusChangeListener(
            object : AppManager.OnActivityStatusChangeListener() {
                override fun onActivityCreate(activity: BaseActivity) {
                    super.onActivityCreate(activity)
                    Log.i(">>>", "Activity: $activity 已创建")
                }

                override fun onActivityDestroy(activity: BaseActivity) {
                    super.onActivityDestroy(activity)
                    Log.i(">>>", "Activity: $activity 已销毁")
                }

                override fun onAllActivityClose() {
                    Log.i(">>>", "所有Activity已经关闭")
                }
            }
        )
    }

    /**
     * Configures the given [io.flutter.embedding.engine.FlutterEngine].
     *
     *
     * This method is called after the given [io.flutter.embedding.engine.FlutterEngine] has
     * been attached to the owning `FragmentActivity`. See [ ][io.flutter.embedding.engine.plugins.activity.ActivityControlSurface.attachToActivity].
     *
     *
     * It is possible that the owning `FragmentActivity` opted not to connect itself as an
     * [io.flutter.embedding.engine.plugins.activity.ActivityControlSurface]. In that case, any
     * configuration, e.g., plugins, must not expect or depend upon an available `Activity` at
     * the time that this method is invoked.
     *
     * [flutterEngine] The Flutter engine.
     */
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        val registry = flutterEngine.platformViewsController.registry
        registry.registerViewFactory("android_view", LinkNativeViewFactory())

        val messenger = flutterEngine.dartExecutor.binaryMessenger
        val channel = MethodChannel(messenger, "android")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {
                "menu" -> {
                    // 事件
                    res.success("success")
                }

                else -> {
                    res.error("error", "error_message", null)
                }
            }
        }
    }

    /**
     * Cleans up references that were established in [.configureFlutterEngine]
     * before the host is destroyed or detached.
     *
     * [flutterEngine] The Flutter engine.
     */
    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    /**
     * 如果是默认桌面就拦截返回指令
     * 通过调用FlutterFragment提供的返回代码进行返回操作，需要flutterFragment初始化一次后生效，当Flutter退回到首页后才执行安卓原有的返回操作
     */
    override fun onBack(): Boolean {
        super.onBack()
        if (!isHome) {
            mFlutterFragment.onBackPressed()
        }
        log("操你妈的执行了")
        return isHome
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            //android.R.id.home -> if (!isHome) onBack()
            R.id.action_settings -> {
                current(item = ContentAdapter.settings)
            }
        }
        return true
    }

    override fun onPostResume() {
        super.onPostResume()
        mFlutterFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mFlutterFragment.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mFlutterFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        mFlutterFragment.onUserLeaveHint()
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        mFlutterFragment.onTrimMemory(level)
    }

    private fun onRequestPermissionsResults(requestCode: Int, grantResult: Int) {
        if (
            grantResult == PackageManager.PERMISSION_GRANTED
        ) {

        }
    }

    private fun check() {
        try {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0)
            }
        } catch (e: Exception) {
            if (e.javaClass == IllegalStateException().javaClass) {
                toast("Shizuku未激活")
            }
        }
    }

    private fun initSP() {
        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mBaseContext)

//        isFull = mSharedPreferences.getBoolean(
//            "running_mode",
//            true
//        )

        isDynamicColor = mSharedPreferences.getBoolean(
            "dynamic_colors",
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    /**
     * 加载Ui控件
     */
    private fun initUi() {
        // 加载AppBarLayout
        mAppBarLayout = AppBarLayout(
            mBaseContext
        ).apply {
            fitsSystemWindows = true
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            addView(
                MaterialToolbar(
                    mBaseContext
                ).apply {
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    navigationIcon = ContextCompat.getDrawable(
                        mBaseContext,
                        R.drawable.baseline_menu_24
                    )
                    logo = ContextCompat.getDrawable(
                        mBaseContext,
                        R.drawable.baseline_terminal_24
                    )
                }.also { toolbar ->
                    setSupportActionBar(toolbar)
                    mToolbar = toolbar
                },
                AppBarLayout.LayoutParams(
                    AppBarLayout.LayoutParams.MATCH_PARENT,
                    AppBarLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }.also { appBar ->
            appBar.isLiftOnScroll = true
            appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                mBaseContext
            )
        }

        // 加载ViewPager2
        mViewPager2 = ViewPager2(
            mBaseContext
        ).apply {
            isUserInputEnabled = false
        }

        // 加载TabLayout
        mTabLayout = TabLayout(
            mBaseContext
        ).apply {
            tabMode = TabLayout.MODE_AUTO
        }
    }

    @SuppressLint("RestrictedApi")
    @Suppress("DEPRECATION")
    private fun initSystemBar() {
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val decorView = window.decorView
        val visibility: Int = decorView.systemUiVisibility
        decorView.systemUiVisibility = visibility or option

        setDarkStatusBarTheme(true)
        setDarkNavigationBarTheme(true)
        setNavigationBarBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private fun initFlutter() {
        // 创建Flutter引擎缓存
        val flutterEngine = FlutterEngine(mBaseContext)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(termplux_flutter, flutterEngine)

        // 初始化FlutterFragment
        mFlutterFragment = FlutterFragment
            .withCachedEngine(termplux_flutter)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(true)
            .build()
    }

    private fun initServices() {
        val intent = Intent(mBaseContext, MainService().javaClass)
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

//    private fun fullScreenWebView(url: String) {
//        FullScreenDialog.show(
//            object : OnBindView<FullScreenDialog>(mWebView) {
//                override fun onBind(dialog: FullScreenDialog?, v: View?) {
//                    mWebView.loadUrl(url)
//                }
//            }
//        )
//    }

    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    private fun toggle() {
        if (mVisible) hide() else show()
    }

    private fun hide() {
        supportActionBar?.hide()
        mVisible = false
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, uiAnimatorDelay.toLong())
    }

    @Suppress("DEPRECATION")
    private fun show() {
        if (Build.VERSION.SDK_INT >= 30) {
            mComposeView.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            mComposeView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        mVisible = true
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
    }

    /**
     * ViewPager2的适配器
     */
    private fun adapter(
        navController: NavHostController,
        scope: CoroutineScope,
        drawerState: DrawerState
    ) {
        // 初始化适配器实例
        val adapter = ContentAdapter.newInstance(
            activity = mME,
            flutter = mFlutterFragment,
            viewPager = mViewPager2,
            appBarLayout = mAppBarLayout,
            drawerState = { state ->
                scope.launch {
                    when (state) {
                        true -> drawerState.open()
                        false -> drawerState.close()
                    }
                }
            },
            navigation = { route ->
                navController.navigate(
                    route = route
                ) {
                    popUpTo(
                        id = navController.graph.findStartDestination().id
                    ) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        // 获取页面个数
        val count = adapter.itemCount
        // 设置适配器
        mViewPager2.adapter = adapter
        // 预加载页面
        mViewPager2.offscreenPageLimit = count
    }

    private fun syncDrawer(
        scope: CoroutineScope,
        drawerState: DrawerState
    ) {
        mToolbar.setNavigationOnClickListener {
            scope.launch {
                drawerState.open()
            }
        }
    }

    private fun mediator() {
        val title = arrayOf(
            getString(R.string.menu_launcher),
            getString(R.string.menu_home),
            getString(R.string.menu_apps),
            getString(R.string.menu_settings)
        )
        TabLayoutMediator(
            mTabLayout, mViewPager2
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = title[position]
        }.attach()
    }

//    @Composable
//    private fun AppBarLayout(modifier: Modifier) {
//        AndroidView(
//            factory = {
//                mAppBarLayout
//            },
//            modifier = modifier
//        )
//    }

    /**
     * 标签栏
     */
    @Composable
    private fun TabLayout(modifier: Modifier) {
        AndroidView(
            factory = {
                mTabLayout
            },
            modifier = modifier
        )
    }

    /**
     * 主页内容
     */
    @Composable
    private fun ViewPager2(modifier: Modifier) {
        AndroidView(
            factory = {
                mViewPager2
            },
            modifier = modifier
        )
    }

    /**
     * ViewPager2切换页面
     */
    private fun current(item: Int) {
        if (mViewPager2.currentItem != item) {
            mViewPager2.currentItem = item
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
        if (mVisible) mToolbar.showOverflowMenu() else {
            show()
            mToolbar.showOverflowMenu()
        }
    }

    // 打开任务栏设置
    private fun taskbar() {
        Taskbar.openSettings(
            mBaseContext,
            getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }


    /**
     * 子类的入口函数
     */
    abstract fun onCreated(parameter: JumpParameter?)

    /**
     * 设置页面内容，仅在完整模式下生效
     */
    protected fun setContents(
        content: @Composable (
            navController: NavHostController,
            drawerState: DrawerState,
            content: @Composable (content: String, modifier: Modifier) -> Unit,
            event: (event: String) -> Unit,
            message: (message: String) -> String,
            current: (item: Int) -> Unit,
            browser: (url: String) -> Unit
        ) -> Unit
    ) {
        mComposeView.apply {
            // 设置内容
            setContent {
                // 导航控制器实例
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(
                    initialValue = DrawerValue.Closed
                )
                // 系统界面控制器实例
                val systemUiController = rememberSystemUiController()
                // 判断系统是否处于深色模式
                val darkTheme = isSystemInDarkTheme()
                // 配色方案
                val colorScheme = when {
                    isDynamicColor -> {
                        if (darkTheme) dynamicDarkColorScheme(
                            context = mBaseContext
                        ) else dynamicLightColorScheme(
                            context = mBaseContext
                        )
                    }

                    darkTheme -> DarkColorScheme
                    else -> LightColorScheme
                }
                // 设置适配器
                adapter(
                    navController = navController,
                    scope = scope,
                    drawerState = drawerState
                )
                syncDrawer(
                    scope = scope,
                    drawerState = drawerState
                )
                mediator()
                // 设置系统界面样式
                if (!mComposeView.isInEditMode) {
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = !darkTheme
                        )
                        WindowCompat.getInsetsController(
                            window,
                            mComposeView
                        )
                    }
                }
                // 设置页面样式和内容
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = Typography,
                ) {
                    content(
                        navController = navController,
                        drawerState = drawerState,
                        content = { content, modifier ->
                            when (content) {
                                tabBar -> TabLayout(modifier = modifier)
                                pager -> ViewPager2(modifier = modifier)
                            }
                        },
                        event = { event ->
                            when (event) {
                                toggle -> toggle()
                                taskbar -> taskbar()
                                options -> optionsMenu()
                            }
                        },
                        message = { message ->
                            when (message) {
                                shizukuVersion -> getShizukuVersion()
                                androidVersion -> getAndroidVersion()
                                else -> ""
                            }
                        },
                        current = { item ->
                            current(
                                item = item
                            )
                        },
                        browser = { url ->

                        }
                    )
                }
            }
        }
    }

    companion object {

        const val tabBar: String = "tabBar"
        const val flutter: String = "flutter"
        const val apps: String = "apps"

        const val pager: String = "pager"

        const val toggle: String = "toggle"
        const val taskbar: String = "taskbar"
        const val options: String = "options"

        const val shizukuVersion: String = "shizukuVersion"
        const val androidVersion: String = "androidVersion"

        // 浅色模式配色
        private val Purple80 = Color(0xFFD0BCFF)
        private val PurpleGrey80 = Color(0xFFCCC2DC)
        private val Pink80 = Color(0xFFEFB8C8)

        // 深色模式配色
        private val Purple40 = Color(0xFF6650a4)
        private val PurpleGrey40 = Color(0xFF625b71)
        private val Pink40 = Color(0xFF7D5260)

        // 样式
        private val Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )

        // 深色模式
        private val DarkColorScheme = darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        // 浅色模式
        private val LightColorScheme = lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300

        const val termplux_flutter = "termplux_flutter"
    }
}