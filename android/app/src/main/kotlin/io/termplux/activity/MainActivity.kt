package io.termplux.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnBindView
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.BSD3ClauseLicense
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.termplux.BuildConfig
import io.termplux.IUserService
import io.termplux.R
import io.termplux.adapter.AppsAdapter
import io.termplux.adapter.FlutterAdapter
import io.termplux.custom.ScrollControllerWebView
import io.termplux.receiver.MainReceiver
import io.termplux.services.MainService
import io.termplux.services.UserService
import io.termplux.ui.ActivityMain
import kotlinx.coroutines.Runnable
import rikka.shizuku.Shizuku
import kotlin.math.hypot
import kotlin.system.exitProcess

class MainActivity : BaseActivity(), FlutterEngineConfigurator, Runnable {

    private val mContext: BaseActivity = me

    private var mViewContext: Context = mContext
    private var mThisContext: Context = mContext

    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mComposeView: ComposeView
    private lateinit var mAppsGridView: GridView
    private lateinit var mFlutterViewPager: ViewPager2
    private lateinit var mWebView: ScrollControllerWebView

    private lateinit var userServices: IUserService
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mIntentFilter: IntentFilter
    private lateinit var mAppsList: List<ResolveInfo>
    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private lateinit var flutterFragment: FlutterFragment

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


    private val mHandler = Handler(
        Looper.myLooper()!!
    )

    private var mVisible: Boolean = false

    private val showPart2Runnable = Runnable {
        supportActionBar?.show()
    }

    private val hideRunnable = Runnable {
        hide()
    }

    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (autoHide) {
                delayedHide(autoHideDelayMillis)
            }
            MotionEvent.ACTION_UP -> view.performClick()
        }
        false
    }

    private val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )

    private val purple80: Color = Color(0xFFD0BCFF)
    private val purpleGrey80: Color = Color(0xFFCCC2DC)
    private val pink80: Color = Color(0xFFEFB8C8)

    private val purple40: Color = Color(0xFF6650a4)
    private val purpleGrey40: Color = Color(0xFF625b71)
    private val pink40: Color = Color(0xFF7D5260)

    private val darkColorScheme = darkColorScheme(
        primary = purple80,
        secondary = purpleGrey80,
        tertiary = pink80
    )

    private val lightColorScheme = lightColorScheme(
        primary = purple40,
        secondary = purpleGrey40,
        tertiary = pink40
    )

    /** 用户协议状态 */
    private var mLicence: Boolean by mutableStateOf(true)

    /** 是否使用动态颜色 */
    private var mDynamicColor: Boolean by mutableStateOf(true)

    /** 是否使用完整桌面 */
    private var mLibTaskBar: Boolean by mutableStateOf(true)

    /** 应用运行模式 */
    private var mMode: Int by mutableStateOf(0)

    /** 目标应用 */
    private val mTarget: Int by mutableStateOf(0)


    private var targetAppName: String by mutableStateOf("unknown")
    private var targetPackageName: String by mutableStateOf("unknown")
    private var targetDescription: String by mutableStateOf("unknown")

    /**
     ***********************************************************************************************
     * 生命周期函数
     ***********************************************************************************************
     */

    @SuppressLint("SetJavaScriptEnabled")
    override fun resetContentView(): View {
        super.resetContentView()
        // 加载屏闪动画ImageView
        mSplashLogo = AppCompatImageView(
            mViewContext
        ).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.custom_termplux_24
                )
            )
        }

        // 应用栏槽位
        mAppBarLayout = AppBarLayout(
            mViewContext
        ).apply {
            fitsSystemWindows = true
            isLiftOnScroll = true
        }

        // 工具栏
        mToolbar = MaterialToolbar(
            mViewContext
        ).apply {
            navigationIcon = ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.baseline_arrow_back_24
            )
        }

        // Compose主界面
        mComposeView = ComposeView(
            context = mViewContext
        ).apply {
            setOnTouchListener(delayHideTouchListener)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }

        // 应用列表
        mAppsGridView = GridView(
            mViewContext
        ).apply {
            numColumns = GridView.AUTO_FIT
            columnWidth = resources.getDimension(R.dimen.app_width).toInt()
        }

        mFlutterViewPager = ViewPager2(
            mViewContext
        ).apply {
            isUserInputEnabled = false
        }

        mWebView = ScrollControllerWebView(
            context = mViewContext
        ).apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.allowFileAccess = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadsImagesAutomatically = true
            settings.defaultTextEncodingName = "utf-8"
        }

        // 返回应用主界面布局
        return LinearLayoutCompat(
            this@MainActivity
        ).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                mAppBarLayout.apply {
                    addView(
                        mToolbar,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    )
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                mComposeView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun initViews() {

        // 更新操作栏状态
        mVisible = true

        // 布局参数
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        // 设置操作栏
        setSupportActionBar(mToolbar)

        // 添加开屏动画
        addContentView(mSplashLogo, layoutParams)

        mComposeView.visibility = View.INVISIBLE
        mComposeView.post(this@MainActivity)


        initPreferences()

        initAppsList()
        receiveSystemCast()

        initFlutterEngine()
        initFlutter()

        initUi()
        initSystemBar()


        check()

        initCompose()
        initServices()


    }

    override fun initDatas(parameter: JumpParameter?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {

            }
        }
    }

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            object : LifeCircleListener() {
                override fun onCreate() {
                    super.onCreate()
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
                    mComposeView.removeCallbacks(this@MainActivity)
                    Shizuku.removeBinderReceivedListener(binderReceivedListener)
                    Shizuku.removeBinderDeadListener(binderDeadListener)
                    Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
                    unregisterReceiver(mBroadcastReceiver)
                    unbindService(mConnection)
                    Shizuku.unbindUserService(mUserServiceArgs, mUserServiceConnection, true)
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

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun run() {
        val cx = mSplashLogo.x + mSplashLogo.width / 2f
        val cy = mSplashLogo.y + mSplashLogo.height / 2f
        val startRadius = hypot(
            x = mSplashLogo.width.toFloat(),
            y = mSplashLogo.height.toFloat()
        )
        val endRadius = hypot(
            x = mComposeView.width.toFloat(),
            y = mComposeView.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mComposeView,
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
                mComposeView.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    override fun onPostResume() {
        super.onPostResume()
        flutterFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        flutterFragment.onNewIntent(intent)
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        flutterFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        flutterFragment.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        flutterFragment.onTrimMemory(level)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.action_settings -> {
                PopTip.show("你小子6")
                if (mVisible) {
                    hide()
                }
            }
        }
        return true
    }

    /**
     ***********************************************************************************************
     * 加载函数
     ***********************************************************************************************
     */

    private fun onRequestPermissionsResults(requestCode: Int, grantResult: Int) {
        if (
            grantResult == PackageManager.PERMISSION_GRANTED
        ) {

        }
    }


    /**
     * 加载首选项
     */
    private fun initPreferences() {
        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        // 获取用户协议状态true为已签署，false为未签署
        mLicence = mSharedPreferences.getBoolean(
            licence,
            true
        )
        // 获取是否启用动态颜色
        mDynamicColor = mSharedPreferences.getBoolean(
            dynamicColor,
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        // 获取是否使用完整桌面模式
        mLibTaskBar = mSharedPreferences.getBoolean(
            libTaskBar,
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }


    @Suppress("DEPRECATION")
    private fun initAppsList() {
        // 加载应用列表
        mAppsList = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
        )

        // 适配器
        val appsAdapter: BaseAdapter = AppsAdapter(
            list = mAppsList,
            context = mViewContext
        )

        //设置适配器。
        mAppsGridView.apply {
            adapter = appsAdapter
        }
    }

    private fun receiveSystemCast() {
        mIntentFilter = IntentFilter()
        mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED")
        mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED")
        mIntentFilter.addDataScheme("package")

        mBroadcastReceiver = MainReceiver(
            refresh = {
                initAppsList()
            }
        )

        registerReceiver(mBroadcastReceiver, mIntentFilter)
    }

    private fun initFlutterEngine() {
        // 创建Flutter引擎缓存
        val flutterEngine = FlutterEngine(mThisContext)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(flutter_engine, flutterEngine)
    }

    private fun initFlutter() {
        // 初始化FlutterFragment
        flutterFragment = FlutterFragment
            .withCachedEngine(flutter_engine)
            .renderMode(RenderMode.texture)
            .transparencyMode(TransparencyMode.transparent)
            .build()

        // 适配器
        val flutter: FragmentStateAdapter = FlutterAdapter(
            activity = mContext,
            position = 1,
            flutterFragment = flutterFragment
        )

        // 设置适配器
        mFlutterViewPager.apply {
            adapter = flutter
            offscreenPageLimit = flutter.itemCount
        }
    }

    /**
     * 加载用户界面
     */
    private fun initUi() {
        // 设置工具栏前景色
        mAppBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(mThisContext)
        // 工具栏
        mToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    @SuppressLint("RestrictedApi")
    @Suppress("DEPRECATION")
    private fun initSystemBar() {
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
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


    private fun check() {
        try {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0)
            }
        } catch (e: Exception) {
            if (e.javaClass == IllegalStateException().javaClass) {
                Toast.makeText(
                    this,
                    "Shizuku未激活",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    /**
     * 加载应用主界面
     */
    private fun initCompose() {
        mComposeView.apply {
            setContent {
                // 获取系统是否为深色模式
                val darkTheme = isSystemInDarkTheme()
                // 初始化系统界面控制工具
                val systemUiController = rememberSystemUiController()
                // 颜色
                val colorScheme: ColorScheme = when {
                    mDynamicColor -> {
                        if (darkTheme) {
                            dynamicDarkColorScheme(
                                context = mViewContext
                            )
                        } else {
                            dynamicLightColorScheme(
                                context = mViewContext
                            )
                        }
                    }
                    darkTheme -> darkColorScheme
                    else -> lightColorScheme
                }
                // 系统界面设置
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
                // 主题和内容
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = typography,
                ) {
                    ActivityMain(
                        onOptionsMenu = {
                            overflowMenu()
                        },
                        onToggle = {
                            toggle()
                        },
                        androidVersion = getAndroidVersion(),
                        shizukuVersion = getShizukuVersion(),
                        gridView = mAppsGridView,
                        appsList = mAppsList,
                        isSystemApps = { packageName ->
                            isSystemApplication(
                                packageName = packageName
                            )
                        },
                        startApp = { packageName, className ->
                            startApplication(
                                packageName = packageName,
                                className = className
                            )
                        },
                        infoApp = { packageName ->
                            infoApplication(
                                packageName = packageName
                            )
                        },
                        deleteApp = { packageName ->
                            deleteApplication(
                                packageName = packageName
                            )
                        },
                        targetAppVersionName = "",
                        viewPager = mFlutterViewPager,
                        dynamicColorChecked = mDynamicColor,
                        taskBarChecked = mLibTaskBar,
                        onDynamicChecked = { value ->

                        },
                        onTaskBarChecked = { value ->

                        },
                        onTaskBarSettings = {
                            taskbarSettings()
                        },
                        onSystemSettings = {
                            systemSettings()
                        },
                        onDefaultLauncherSettings = {
                            defaultLauncher()
                        },

                        onEasterEgg = {
                            easterEgg()
                        },
                        onNotice = {
                            licenseDialog()
                        },
                        onSource = {
                            fullScreenWebView("https://github.com/TermpPlux/TermPlux-App")
                        },
                        onDevGitHub = {
                            fullScreenWebView("https://github.com/wyq0918dev")
                        },
                        onDevTwitter = {
                            fullScreenWebView("https://twitter.com/wyq0918dev")
                        },
                        onTeamGitHub = {
                            fullScreenWebView("https://github.com/TermPlux")
                        }
                    )
                }
            }
        }
    }

    private fun initServices() {
        val intent = Intent(this@MainActivity, MainService().javaClass)
        startService(intent)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     ***********************************************************************************************
     * 其他函数
     ***********************************************************************************************
     */

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

    private fun fullScreenWebView(url: String) {
        FullScreenDialog.show(
            object : OnBindView<FullScreenDialog>(mWebView) {
                override fun onBind(dialog: FullScreenDialog?, v: View?) {
                    mWebView.loadUrl(url)
                }
            }
        )
    }

    private fun licenseDialog() {
        val notices = Notices()
        notices.addNotice(
            Notice(
                "AndroidUtilCode",
                "https://github.com/Blankj/AndroidUtilCode",
                "Copyright Blankj",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "Flutter",
                "https://github.com/flutter/flutter",
                "Copyright Google",
                BSD3ClauseLicense()
            )
        )
        notices.addNotice(
            Notice(
                "LibTaskBar",
                "https://github.com/farmerbb/libtaskbar",
                "Copyright farmerbb",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "BaseFramework",
                "https://github.com/kongzue/BaseFramework",
                "Copyright BaseFramework",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "DialogX",
                "https://github.com/kongzue/DialogX",
                "Copyright Kongzue DialogX",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "AndroidHiddenApiBypass",
                "https://github.com/LSPosed/AndroidHiddenApiBypass",
                "Copyright 2021-2023 LSPosed",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "FakeStore",
                "https://github.com/microg/FakeStore",
                "Copyright 2013-2016 microG Project Team",
                ApacheSoftwareLicense20()
            )
        )
        notices.addNotice(
            Notice(
                "Shizuku-API",
                "https://github.com/RikkaApps/Shizuku-API",
                "Copyright RikkaApps",
                MITLicense()
            )
        )
        notices.addNotice(
            Notice(
                "UserLAnd",
                "https://github.com/CypherpunkArmory/UserLAnd",
                "Copyright CypherpunkArmory",
                ApacheSoftwareLicense20()
            )
        )
        LicensesDialog.Builder(mContext)
            .setNotices(notices)
            .setIncludeOwnLicense(true)
            .build()
            .show()
    }

    private fun startApplication(
        packageName: String,
        className: String
    ) {
        try {
            // 启动目标应用
            val intent = Intent()
            intent.component = ComponentName(packageName, className)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            // 如果已卸载但未刷新跳转应用市场防止程序崩溃
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun infoApplication(packageName: String) {
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Exception) {
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun deleteApplication(packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Exception) {
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun openApplicationMarket(packageName: String) {
        val str = "market://details?id=$packageName"
        val localIntent = Intent(Intent.ACTION_VIEW)
        localIntent.data = Uri.parse(str)
        startActivity(localIntent)
    }

    /**
     * 检测应用是否为系统应用
     */
    private fun isSystemApplication(packageName: String): Boolean {
        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_CONFIGURATIONS
            )
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    private fun easterEgg() {
        val intent = Intent(mThisContext, FlutterActivity().javaClass)
        startActivity(intent)
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
        return "Shizuku 13"
    }

    // 打开任务栏设置
    private fun taskbarSettings() {
        Taskbar.openSettings(
            mThisContext,
            mContext.getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }

    // 选择默认主屏幕应用
    private fun defaultLauncher() {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // 打开系统设置
    private fun systemSettings() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // 检查设备是否支持谷歌基础服务
    fun checkGooglePlayServices(
        onNoGms: () -> Unit
    ) {
        val code =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MainActivity)
        if (code != ConnectionResult.SUCCESS) {
            if (!AppUtils.isAppInstalled("")) {
                GoogleApiAvailability.getInstance()
                    .makeGooglePlayServicesAvailable(this@MainActivity)
                if (GoogleApiAvailability.getInstance().isUserResolvableError(code)) {
                    onNoGms()
                }
            }
        }
    }


    private fun killAppProcess() {
        val mActivityManager = getSystemService(BaseApp.ACTIVITY_SERVICE) as ActivityManager
        val mList = mActivityManager.runningAppProcesses
        for (runningAppProcessInfo in mList) {
            if (runningAppProcessInfo.pid != Process.myPid()) {
                Process.killProcess(runningAppProcessInfo.pid)
            }
        }
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    private fun overflowMenu() {
        if (mVisible) {
            mToolbar.showOverflowMenu()
        } else {
            show()
            mToolbar.showOverflowMenu()
        }
    }

    private fun flutterBack() {
        flutterFragment.onBackPressed()
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    @Suppress("DEPRECATION")
    private fun show() {
        mVisible = true
        mHandler.postDelayed(
            showPart2Runnable,
            uiAnimatorDelay.toLong()
        )
    }

    private fun hide() {
        supportActionBar?.hide()
        mVisible = false
        mHandler.removeCallbacks(showPart2Runnable)
    }

    private fun delayedHide(delayMillis: Int) {
        mHandler.removeCallbacks(hideRunnable)
        mHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    private external fun fuck()

    companion object {

        init {
            // 加载C++代码
            System.loadLibrary("termplux")
        }

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800

        const val flutter_engine = "termplux_flutter"

        const val none: Int = 0
        const val shizuku: Int = 1

        const val licence: String = "licence"
        const val dynamicColor: String = "dynamic_colors"
        const val libTaskBar: String = "desktop"


        // Shizuku
        const val shizukuPackage: String = "moe.shizuku.privileged.api"

        // Linux运行环境
        const val termux: String = "com.termux"

        // 谷歌基础服务
        const val gms: String = "com.google.android.gms"
        const val google: String = "com.google.android.googlequicksearchbox"
    }
}