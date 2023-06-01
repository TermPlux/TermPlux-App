package io.termplux.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.FoldingFeature
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.dialogx.dialogs.PopTip
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.termplux.BuildConfig
import io.termplux.IUserService
import io.termplux.R
import io.termplux.adapter.AppsAdapter
import io.termplux.adapter.PagerAdapter
import io.termplux.adapter.SettingsAdapter
import io.termplux.model.AppsModel
import io.termplux.receiver.AppsReceiver
import io.termplux.services.MainService
import io.termplux.services.UserService
import io.termplux.ui.ActivityMain
import io.termplux.ui.window.ContentType
import io.termplux.ui.window.DevicePosture
import io.termplux.ui.window.NavigationType
import io.termplux.ui.window.isBookPosture
import io.termplux.ui.window.isSeparating
import kotlinx.coroutines.Runnable
import rikka.shizuku.Shizuku
import kotlin.math.abs
import kotlin.math.hypot

class MainFragment : FlutterBoostFragment(), Runnable {

    // 平台通道
    private lateinit var channel: MethodChannel

    // 上下文
    private lateinit var mActivityContext: FragmentActivity
    private lateinit var mFragmentContext: FlutterBoostFragment

    // 首选项
    private lateinit var mSharedPreferences: SharedPreferences
    private var isDynamicColor: Boolean by mutableStateOf(value = true)

    // Shizuku用户服务
    private lateinit var userServices: IUserService

    // shizuku进程已激活
    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {}

    // shizuku进程被停止
    private val binderDeadListener = Shizuku.OnBinderDeadListener {}

    //shizuku监听授权结果
    private val requestPermissionResultListener =
        Shizuku.OnRequestPermissionResultListener { requestCode: Int, grantResult: Int ->
            onRequestPermissionsResults(requestCode, grantResult)
        }

    // 视图控件
    private lateinit var mRootView: View
    private lateinit var mViewPager: ViewPager2
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mComposeView: ComposeView
    private lateinit var mRecyclerView: RecyclerView

    private val hideHandler = Handler(
        Looper.myLooper()!!
    )
    private val showPart2Runnable = Runnable {
        (mActivityContext as AppCompatActivity).supportActionBar?.show()
    }
    private var mVisible: Boolean = false
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


    private lateinit var appReceiver: BroadcastReceiver
    private lateinit var intentFilter: IntentFilter

    /**
     * Flutter引擎配置，该方法有onAttach方法调用
     */
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        // 初始化平台通道
        val messenger = flutterEngine.dartExecutor.binaryMessenger
        channel = MethodChannel(messenger, "termplux_channel")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取上下文
        mActivityContext = requireActivity()
        mFragmentContext = this@MainFragment
        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivityContext)
        // 获取动态颜色首选项
        isDynamicColor = mSharedPreferences.getBoolean(
            "dynamic_colors",
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        // 初始化用户服务
        initServices()
        // 检查权限
        check()
        // 注册监听器
        Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(requestPermissionResultListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 返回根帧布局
        return FrameLayout(mActivityContext).apply {
            // 添加控件
            addView(
                ViewPager2(mActivityContext).apply {
                    // 初始值为否，会在动画结束后恢复是，目的是防止动画加载过程中用户操作导致错误
                    isUserInputEnabled = false
                    // 设置触摸监听
                    setOnTouchListener(delayHideTouchListener)
                    // 设置方向为水平
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    // 设置切换动画
                    setPageTransformer { page, position ->
                        page.apply {
                            when {
                                position < -1 -> {
                                    // 禁用圆角动画
                                    clipToOutline = false
                                    // 不透明
                                    alpha = 0f
                                }

                                position <= 1 -> {
                                    val scaleFactor = minScale.coerceAtLeast(1 - abs(position))
                                    val verticalMargin = height * (1 - scaleFactor) / 2
                                    val horizontalMargin = width * (1 - scaleFactor) / 2
                                    // 获取圆角大小
                                    val round = resources.getDimension(R.dimen.pager_round).toInt()
                                    // 深景缩放动画
                                    translationX = if (position < 0) {
                                        horizontalMargin - verticalMargin / 2
                                    } else {
                                        horizontalMargin + verticalMargin / 2
                                    }
                                    // 滑动监听
                                    scaleX = scaleFactor
                                    scaleY = scaleFactor
                                    // 启用圆角动画
                                    clipToOutline = true
                                    outlineProvider = object : ViewOutlineProvider() {
                                        override fun getOutline(view: View, outline: Outline) {
                                            outline.setRoundRect(
                                                0,
                                                0,
                                                view.width,
                                                view.height,
                                                (round + (((scaleFactor - minScale) / (1 - minScale)) * (1 - round)))
                                            )
                                        }
                                    }
                                    // 透明度
                                    alpha =
                                        (minAlpha + (((scaleFactor - minScale) / (1 - minScale)) * (1 - minAlpha)))
                                }

                                else -> {
                                    // 禁用圆角动画
                                    clipToOutline = false
                                    // 不透明
                                    alpha = 0f
                                }
                            }
                        }
                    }
                    // 设置适配器
                    adapter = PagerAdapter(
                        rootView = super.onCreateView(
                            inflater,
                            container,
                            savedInstanceState
                        )?.let { root ->
                            root.apply {
                                visibility = View.INVISIBLE
                                post(mFragmentContext as Runnable)
                            }.also { view ->
                                mRootView = view
                            }
                        },
                        composeView = ComposeView(mActivityContext).apply {
                            setViewCompositionStrategy(
                                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                            )
                        }.also { compose ->
                            mComposeView = compose
                        },
                        swipeRefreshLayout = SwipeRefreshLayout(mActivityContext).apply {
                            fitsSystemWindows = true
                            addView(
                                RecyclerView(mActivityContext).apply {
                                    layoutManager = GridLayoutManager(
                                        mActivityContext,
                                        4,
                                        RecyclerView.VERTICAL,
                                        false
                                    )
                                }.also { apps ->
                                    mRecyclerView = apps
                                },
                                ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            )
                            setOnRefreshListener {
                                // 执行下滑刷新事件
                                PopTip.show("下拉刷新")

                                isRefreshing = false
                            }
                        },
                        viewPager = ViewPager2(mActivityContext).apply {
                            fitsSystemWindows = true
                            // 此页面仅展示设置，禁止用户滑动
                            isUserInputEnabled = false
                            // 设置方向为水平
                            orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            // 设置适配器
                            adapter = SettingsAdapter(
                                mFragmentContext,
                                settings = {}
                            )
                        }
                    )
                    // 预加载全部页面
                    offscreenPageLimit = PagerAdapter.count
                }.also { pager ->
                    mViewPager = pager
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            // 添加控件
            addView(
                AppCompatImageView(mActivityContext).apply {
                    // 设置图片资源
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
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
        }
    }

    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mRootView.width.toFloat(),
            y = mRootView.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mRootView,
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
                mRootView.visibility = View.VISIBLE
                // 动画结束时启用ViewPager2的用户操作
                mViewPager.isUserInputEnabled = true
                circularAnim.start()
            }
            .start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化系统界面状态
        mVisible = true
        // 加载应用列表
        loadApp()
        // 意图过滤器
        intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }

        // 用于刷新应用列表的广播接收器
        appReceiver = AppsReceiver {
            loadApp()
        }

        // 注册广播接收器
        mActivityContext.registerReceiver(appReceiver, intentFilter)

        setContent()


    }

    override fun onResume() {
        super.onResume()
        if (mVisible) delayedHide(100)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mActivityContext.unregisterReceiver(appReceiver)
        mRootView.removeCallbacks(mFragmentContext as Runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 注销监听
        Shizuku.removeBinderReceivedListener(binderReceivedListener)
        Shizuku.removeBinderDeadListener(binderDeadListener)
        Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
        // 解绑用户服务
        Shizuku.unbindUserService(mUserServiceArgs, mUserServiceConnection, true)
        mActivityContext.unbindService(mConnection)
    }

    private fun initServices() {
        val intent = Intent(mActivityContext, MainService().javaClass)
        mActivityContext.startService(intent)
        mActivityContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
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
                PopTip.show("Shizuku未激活")
            }
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
        mVisible = false
        (mActivityContext as AppCompatActivity).supportActionBar?.hide()
        hideHandler.removeCallbacks(showPart2Runnable)
    }

    private fun show() {
        mVisible = true
        hideHandler.postDelayed(showPart2Runnable, uiAnimatorDelay.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
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

    @SuppressLint("RestrictedApi")
    private fun optionsMenu() {
        if (mVisible) {
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.openOptionsMenu()
        } else {
            show()
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.openOptionsMenu()
        }
    }

    // 打开任务栏设置
    private fun taskbar() {
        Taskbar.openSettings(
            mActivityContext,
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
        for (resolveInfo in mActivityContext.packageManager.queryIntentActivities(
            Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setContent() {
        mComposeView.apply {
            setContent {
                channel.setMethodCallHandler { call, res ->
                    when (call.method) {
                        "pager" -> {
                            //mViewPager2.currentItem = mViewPager2.currentItem + 1
                            PopTip.show("6")
                            res.success("success")

                        }

                        "dynamic_colors" -> res.success(isDynamicColor)
                        "toggle" -> toggle()

                        else -> {
                            res.error("error", "error_message", null)
                        }
                    }
                }

                // 获取根View
                val hostView = LocalView.current
                val window = (hostView.context as Activity).window

                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(
                    initialValue = DrawerValue.Closed
                )

                // 导航控制器实例
                val navController = rememberNavController()


                val windowSize = calculateWindowSizeClass(
                    activity = requireActivity()
                )
                val displayFeatures = calculateDisplayFeatures(
                    activity = requireActivity()
                )

                val navigationType: NavigationType
                val contentType: ContentType

                val foldingFeature =
                    displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

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
                            context = mActivityContext
                        ) else dynamicLightColorScheme(
                            context = mActivityContext
                        )
                    }

                    darkTheme -> DarkColorScheme
                    else -> LightColorScheme
                }
//                adapter(navController = navController)
//                mediator(navigationType = navigationType) {
//
//                }
                // 主机控件触摸事件
                //touch(hostView = hostView)
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
                    ActivityMain(
                        navController = navController,
                        drawerState = drawerState,
                        navigationType = navigationType,
                        contentType = contentType,
                        topBar = {

                        },
                        pager = {
//                            AndroidView(
//                                factory = {
//                                    mViewPager2
//                                },
//                                modifier = it
//                            )
                        },
                        navBar = {

                        },
                        tabRow = {
//                        AndroidView(
//                            factory = {
//                                mTabLayout
//                            },
//                            modifier = it
//                        )
                        },
                        optionsMenu = {
                            // optionsMenu()
                        },
                        androidVersion = getAndroidVersion(),
                        shizukuVersion = getShizukuVersion(),
                        current = { item ->

                        },
                        toggle = {
                            //toggle()
                        }
                    )
                }
            }
        }
    }

    companion object {

        // 浅色模式配色
        private val Purple80 = Color(0xFFD0BCFF)
        private val PurpleGrey80 = Color(0xFFCCC2DC)
        private val Pink80 = Color(0xFFEFB8C8)

        // 深色模式配色
        private val Purple40 = Color(0xFF6650a4)
        private val PurpleGrey40 = Color(0xFF625b71)
        private val Pink40 = Color(0xFF7D5260)

        // 样式
        val Typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
        )

        // 深色模式
        val DarkColorScheme = darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )

        // 浅色模式
        val LightColorScheme = lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )

        const val minScale = 0.85f
        const val minAlpha = 0.8f

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300
    }
}