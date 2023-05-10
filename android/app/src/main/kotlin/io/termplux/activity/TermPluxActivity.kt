package io.termplux.activity

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme
import com.kongzue.baseframework.interfaces.EnterAnim
import com.kongzue.baseframework.interfaces.ExitAnim
import com.kongzue.baseframework.interfaces.FragmentLayout
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorHex
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.FragmentChangeUtil
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
import io.termplux.custom.LinkNativeViewFactory
import io.termplux.fragment.HomeFragment
import io.termplux.services.MainService
import io.termplux.services.UserService
import io.termplux.ui.ActivityMain
import io.termplux.ui.preview.TermPluxPreviews
import io.termplux.ui.window.ContentType
import io.termplux.ui.window.DevicePosture
import io.termplux.ui.window.NavigationType
import io.termplux.ui.window.isBookPosture
import io.termplux.ui.window.isSeparating
import kotlinx.coroutines.Runnable
import rikka.shizuku.Shizuku

@SuppressLint("NonConstantResourceId")
@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColorHex("#FFFFFF")
@FragmentLayout(R.id.fragment_container)
@EnterAnim(enterAnimResId = R.anim.fade, holdAnimResId = R.anim.hold)
@ExitAnim(holdAnimResId = R.anim.hold, exitAnimResId = R.anim.back)
class TermPluxActivity : BaseActivity(), FlutterEngineConfigurator {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

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

    private lateinit var userServices: IUserService
    private lateinit var mSharedPreferences: SharedPreferences

    private var isHome: Boolean by mutableStateOf(value = false)
    private var isFull: Boolean by mutableStateOf(value = false)
    private var mVisible: Boolean by mutableStateOf(value = false)
    private var isDynamicColor: Boolean by mutableStateOf(value = true)

    private lateinit var mFlutterPager: ViewPager

    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mTabLayout: TabLayout

    private lateinit var mFlutterEngine: FlutterEngine

    private lateinit var mFlutterFragment: FlutterFragment


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
        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        // 获取动态颜色首选项
        isDynamicColor = mSharedPreferences.getBoolean(
            "dynamic_colors",
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        // 获取运行模式首选项
        isFull = mSharedPreferences.getBoolean(
            "full_mode",
            true
        )
        // 初始化ViewPager
        mFlutterPager = ViewPager(mContext).apply {
            id = R.id.fragment_container
        }
        // 返回ViewPager
        return mFlutterPager
    }

    /**
     * initViews会在启动时首先执行，建议在此方法内进行布局绑定、View初始化等操作
     */
    @SuppressLint("RestrictedApi")
    override fun initViews() {
        // 初始化操作栏状态
        mVisible = true

        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 初始化Flutter引擎,创建Flutter引擎缓存
        mFlutterEngine = FlutterEngine(
            mContext
        ).apply {
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }.also { engine ->
            FlutterEngineCache.getInstance().apply {
                put(termplux_flutter, engine)
            }
        }

        // 初始化FlutterFragment
        mFlutterFragment = FlutterFragment
            .withCachedEngine(termplux_flutter)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(true)
            .build()

        // 初始化底部导航
        mBottomNavigationView = BottomNavigationView(
            mContext
        ).apply {
            inflateMenu(R.menu.navigation)
        }

        // 加载AppBarLayout
        mAppBarLayout = AppBarLayout(
            mContext
        ).apply {
            addView(
                MaterialToolbar(
                    mContext
                ).also { toolbar ->
                    // 设置操作栏
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
                mContext
            )
        }

        // 加载TabLayout
        mTabLayout = TabLayout(
            mContext
        ).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            tabMode = TabLayout.MODE_AUTO
        }


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setIcon(R.drawable.baseline_terminal_24)
        actionBar?.setDisplayHomeAsUpEnabled(true)


        initServices()

    }

    /**
     * initData's会在布局加载后执行，建议在此方法内加载数据和处理布局显示数据
     *
     * [parameter] 从其他界面传入的数据，提供GET、SET方法获取这些数据
     */
    override fun initDatas(parameter: JumpParameter?) {
        check()

//        val map = mutableMapOf<MenuItem, Int>()
//        mBottomNavigationView.menu.forEachIndexed { index, item ->
//            map[item] = index
//        }
//        mBottomNavigationView.setOnItemSelectedListener { items ->
//            map[items]?.let { item ->
//                changeFragment(item)
//            }
//            true
//        }
//        fragmentChangeUtil.onFragmentChangeListener = OnFragmentChangeListener { index, _ ->
//            mBottomNavigationView.selectedItemId = mBottomNavigationView.menu[index -1].itemId
//        }


        if (isFull) setContents { navController, displayFeatures, drawerState, navigationType, contentType, topBar, pager, navBar, tabRow, event, message, current, browser ->
            ActivityMain(
                navController = navController,
                drawerState = drawerState,
                navigationType = navigationType,
                contentType = contentType,
                topBar = topBar,
                pager = pager,
                navBar = navBar,
                tabRow = tabRow,
                optionsMenu = {
                    event(options)
                },
                androidVersion = message(androidVersion),
                shizukuVersion = message(shizukuVersion),
                current = current,
                toggle = {
                    event(toggle)
                }
            )
        }
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

    override fun initFragment(fragmentChangeUtil: FragmentChangeUtil?) {
        super.initFragment(fragmentChangeUtil)
        val home = HomeFragment.newInstance(
            flutter = mFlutterFragment
        )
        fragmentChangeUtil?.addFragment(home, true)
        changeFragment(0)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        val registry = flutterEngine.platformViewsController.registry
        registry.registerViewFactory("android_view", LinkNativeViewFactory())

        val messenger = flutterEngine.dartExecutor.binaryMessenger
        val channel = MethodChannel(messenger, "termplux_channel")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {
                // 跳转桌面
                "navToLauncher" -> {
                    //current(item = ContentAdapter.launcher)
                    res.success("success")
                }
                // 显示/隐藏ActionBar
                "toggle" -> {
                    toggle()
                    res.success("success")
                }
                // 打开ActionBar菜单
                "option" -> {
                    optionsMenu()
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
            android.R.id.home -> onBack()
            R.id.action_settings -> {
                //current(item = ContentAdapter.settings)
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
    }

    private fun show() {
        mVisible = true
        hideHandler.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
    }

//    /**
//     * ViewPager2的适配器
//     */
//    private fun adapter(navController: NavHostController) {
//        // 初始化适配器实例
//        val adapter = ContentAdapter.newInstance(
//            activity = mME,
//            flutter = mFlutterFragment,
//            appBarLayout = mAppBarLayout,
//            current = { item ->
//                current(item = item)
//            },
//            navigation = { route ->
//                navController.navigate(
//                    route = route
//                ) {
//                    popUpTo(
//                        id = navController.graph.findStartDestination().id
//                    ) {
//                        saveState = true
//                    }
//                    launchSingleTop = true
//                    restoreState = true
//                }
//            }
//        )
//        // 获取页面个数
//        val count = adapter.itemCount
//        // 设置适配器
//        mViewPager2.adapter = adapter
//        // 预加载页面
//        mViewPager2.offscreenPageLimit = count - 1
//    }


//    private fun bindingDrawer(navigationType: NavigationType, open: () -> Unit) {
//        mToolbar.apply {
//            if (navigationType != NavigationType.PermanentNavigationDrawer) {
//                navigationIcon = ContextCompat.getDrawable(
//                    mContext,
//                    R.drawable.baseline_menu_24
//                )
//                setNavigationOnClickListener {
//                    open()
//                }
//            }
//        }
//    }


//    private fun mediator(navigationType: NavigationType, home: () -> Unit) {
//        val title = arrayOf(
//            getString(R.string.menu_launcher),
//            getString(R.string.menu_home),
//            getString(R.string.menu_apps),
//            getString(R.string.menu_settings)
//        )
//        MediatorUtils(
//            bottomNavigation = mBottomNavigationView,
//            tabLayout = mTabLayout,
//            viewPager = mViewPager2,
//            home = {
//                home()
//            }
//        ) { page, tab, position ->
//            page.apply {
//                orientation = when (navigationType) {
//                    NavigationType.BottomNavigation -> ViewPager2.ORIENTATION_HORIZONTAL
//                    NavigationType.NavigationRail -> ViewPager2.ORIENTATION_VERTICAL
//                    NavigationType.PermanentNavigationDrawer -> ViewPager2.ORIENTATION_VERTICAL
//                }
//                //isUserInputEnabled = navigationType == NavigationType.BottomNavigation
//                isUserInputEnabled = false
//            }
//            tab.apply {
//                text = title[position]
//            }
//        }.attach()
//    }

    private fun touch(hostView: View) {
        hostView.setOnTouchListener(delayHideTouchListener)
    }

//    /**
//     * ViewPager2切换页面
//     */
//    private fun current(item: Int) {
//        if (mViewPager2.currentItem != item) {
//            mViewPager2.currentItem = item
//        }
//    }

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
            mContext,
            getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }

    /**
     * 设置页面内容，仅在完整模式下生效
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setContents(
        content: @Composable (
            navController: NavHostController,
            displayFeatures: List<DisplayFeature>,
            drawerState: DrawerState,
            navigationType: NavigationType,
            contentType: ContentType,
            topBar: @Composable (Modifier) -> Unit,
            pager: @Composable (Modifier) -> Unit,
            navBar: @Composable (Modifier) -> Unit,
            tabRow: @Composable (Modifier) -> Unit,
            event: (event: String) -> Unit,
            message: (message: String) -> String,
            current: (item: Int) -> Unit,
            browser: (url: String) -> Unit
        ) -> Unit
    ) {
        // 设置内容
        setContent {
            // 获取根View
            val hostView = LocalView.current

            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            )

            // 导航控制器实例
            val navController = rememberNavController()


            val windowSize = calculateWindowSizeClass(
                activity = mME
            )
            val displayFeatures = calculateDisplayFeatures(
                activity = mME
            )

            val navigationType: NavigationType
            val contentType: ContentType

            val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

            val foldingDevicePosture = when {
                isBookPosture(
                    foldFeature = foldingFeature
                ) -> DevicePosture.BookPosture(
                    hingePosition = foldingFeature.bounds
                )

                isSeparating(
                    foldFeature = foldingFeature
                ) -> DevicePosture.Separating(
                    hingePosition = foldingFeature.bounds,
                    orientation = foldingFeature.orientation
                )

                else -> DevicePosture.NormalPosture
            }

            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    navigationType = NavigationType.BottomNavigation
                    contentType = ContentType.Single
                }

                WindowWidthSizeClass.Medium -> {
                    navigationType = NavigationType.NavigationRail
                    contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                        ContentType.Dual
                    } else {
                        ContentType.Single
                    }
                }

                WindowWidthSizeClass.Expanded -> {
                    navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                        NavigationType.NavigationRail
                    } else {
                        NavigationType.PermanentNavigationDrawer
                    }
                    contentType = ContentType.Dual
                }

                else -> {
                    navigationType = NavigationType.BottomNavigation
                    contentType = ContentType.Single
                }
            }

            // 系统界面控制器实例
            val systemUiController = rememberSystemUiController()
            // 判断系统是否处于深色模式
            val darkTheme = isSystemInDarkTheme()
            // 配色方案
            val colorScheme = when {
                isDynamicColor -> {
                    if (darkTheme) dynamicDarkColorScheme(
                        context = mContext
                    ) else dynamicLightColorScheme(
                        context = mContext
                    )
                }

                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
            // 主机控件触摸事件
            touch(hostView = hostView)
            // 绑定操作栏与导航抽屉
//            bindingDrawer(
//                navigationType = navigationType,
//                open = {
//                    scope.launch {
//                        drawerState.open()
//                    }
//                }
//            )
            // 设置系统界面样式
            if (!hostView.isInEditMode) {
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme
                    )
                    WindowCompat.getInsetsController(
                        window,
                        hostView
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
                    displayFeatures = displayFeatures,
                    drawerState = drawerState,
                    navigationType = navigationType,
                    contentType = contentType,
                    topBar = {
                        AndroidView(
                            factory = {
                                mAppBarLayout
                            },
                            modifier = it
                        )
                    },
                    pager = {
                        AndroidView(
                            factory = {
                                mFlutterPager
                            },
                            modifier = it
                        )
                    },
                    navBar = {
                        AndroidView(
                            factory = {
                                mBottomNavigationView
                            },
                            modifier = it
                        )
                    },
                    tabRow = {
                        AndroidView(
                            factory = {
                                mTabLayout
                            },
                            modifier = it
                        )
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
//                        current(
//                            item = item
//                        )
                        changeFragment(item)
                    },
                    browser = { url ->
                        PopTip.show(url)
                    }
                )
            }
        }
    }

    @Composable
    @TermPluxPreviews
    private fun ActivityMainPreview() {
        ActivityMain(
            navController = rememberNavController(),
            drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            ),
            navigationType = NavigationType.BottomNavigation,
            contentType = ContentType.Single,
            topBar = { modifier ->

            },
            pager = { modifier ->
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.view_pager_preview
                        ), textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            navBar = { modifier ->

            },
            tabRow = { modifier ->
                Text(
                    text = stringResource(
                        id = R.string.tab_layout_preview
                    ),
                    modifier = modifier,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            optionsMenu = {},
            androidVersion = "Android Tiramisu 13.0",
            shizukuVersion = "Shizuku 13",
            current = {},
            toggle = {}
        )
    }

    companion object {

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