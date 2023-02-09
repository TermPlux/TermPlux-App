package io.termplux.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.setPadding
import androidx.preference.PreferenceManager
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.dialogs.MessageDialog
import io.termplux.R
import io.termplux.services.MainService
import io.termplux.ui.ActivityMain
import io.termplux.ui.screen.*
import io.termplux.utils.MainActivityUtils
import io.termplux.values.Codes
import io.termplux.values.Packages
import kotlinx.coroutines.Runnable
import rikka.shizuku.Shizuku
import kotlin.math.hypot


class MainActivity : BaseActivity(), Runnable {

    /** 工具包 */
    private val mUtils: MainActivityUtils = MainActivityUtils(mContext = this@MainActivity)

    /** 工具包伴生对象 */
    private val mUtilsCompanion: MainActivityUtils.Companion = MainActivityUtils.Companion

    private lateinit var dialog: AlertDialog

    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mTipProgressBar: ProgressBar
    private lateinit var mTipTextView: AppCompatTextView
    private lateinit var mTipDialogLayout: LinearLayoutCompat

    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mComposeView: ComposeView

    private lateinit var mAppsGridView: GridView
    private lateinit var mAppsImageView: AppCompatImageView
    private lateinit var mAppsTextView: AppCompatTextView
    private lateinit var mAppsLinearLayout: LinearLayoutCompat




    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mIntentFilter: IntentFilter
    private lateinit var mAppsAdapter: BaseAdapter
    private lateinit var mAppsList: List<ResolveInfo>
    private lateinit var mBroadcastReceiver: BroadcastReceiver

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        if (Shizuku.isPreV11()) {

        } else {

        }
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {

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
            MotionEvent.ACTION_DOWN -> if (mUtilsCompanion.autoHide) {
                delayedHide(mUtilsCompanion.autoHideDelayMillis)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
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

    private var androidVersion: String by mutableStateOf("unknown")

    private var targetAppName: String by mutableStateOf("unknown")
    private var targetPackageName: String by mutableStateOf("unknown")
    private var targetDescription: String by mutableStateOf("unknown")

    /**
     * 在Activity启动时最先执行，用于初始化空间和设置布局
     */
    @SuppressLint("InflateParams")
    override fun resetContentView(): View {
        super.resetContentView()
        // 屏闪动画
        mSplashLogo = AppCompatImageView(
            this@MainActivity
        ).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_baseline_termplux_24
                )
            )
        }

        // 进度条
        mTipProgressBar = ProgressBar(
            this@MainActivity
        )

        // 文本控件
        mTipTextView = AppCompatTextView(
            this@MainActivity
        ).apply {
            gravity = Gravity.CENTER
            text = "应用正在启动请稍后"
        }

        // 对话框布局
        mTipDialogLayout = LinearLayoutCompat(
            this@MainActivity
        ).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
            setPadding(
                resources.getDimension(
                    R.dimen.tip_padding
                ).toInt()
            )
            addView(
                mTipProgressBar,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                mTipTextView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        // 应用栏槽位
        mAppBarLayout = AppBarLayout(
            this@MainActivity
        ).apply {
            fitsSystemWindows = true
            isLiftOnScroll = true
        }

        // 工具栏
        mToolbar = MaterialToolbar(
            this@MainActivity
        ).apply {
            navigationIcon = ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.ic_baseline_arrow_back_24
            )
        }

        // 主界面
        mComposeView = ComposeView(
            context = this@MainActivity
        ).apply {
            setOnTouchListener(delayHideTouchListener)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }

        // 应用列表
        mAppsGridView = GridView(this@MainActivity).apply {
            numColumns = GridView.AUTO_FIT
            columnWidth = resources.getDimension(R.dimen.app_width).toInt()
        }

        // 应用图标
        mAppsImageView = AppCompatImageView(
            this@MainActivity
        ).apply {
            setPadding(resources.getDimension(R.dimen.icon_padding).toInt())
        }

        // 应用名称
        mAppsTextView = AppCompatTextView(
            this@MainActivity
        ).apply {
            gravity = Gravity.CENTER
        }

        // 应用item布局
        mAppsLinearLayout = LinearLayoutCompat(
            this@MainActivity
        ).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                resources.getDimension(R.dimen.app_width).toInt(),
                resources.getDimension(R.dimen.app_height).toInt()
            )
            orientation = LinearLayoutCompat.VERTICAL
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

        initStatus()

        initSetContentViewPart2()


        val builder = AlertDialog.Builder(this@MainActivity)
        dialog = builder.create()
        dialog.setView(mTipDialogLayout)
        dialog.setCancelable(false)
//        dialog.show()


        initPreferences()
        initAppsList()
        receiveSystemCast()
        initUi()
        initSystemBar()

        initCreate()
        check()

        initCompose()
        initServices()


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
                mUtilsCompanion.splashPart2AnimatorMillis.toLong()
            )
        mSplashLogo.animate()
            .alpha(0f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(
                mUtilsCompanion.splashPart1AnimatorMillis.toLong()
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

    override fun initDatas(parameter: JumpParameter?) {

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
                    unregisterReceiver(mBroadcastReceiver)
                    Shizuku.removeBinderReceivedListener(binderReceivedListener)
                    Shizuku.removeBinderDeadListener(binderDeadListener)
                    Shizuku.removeRequestPermissionResultListener(requestPermissionResultListener)
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return super.onOptionsItemSelected(item)
    }

    /**
     ***********************************************************************************************
     */

    private fun initStatus(){
        // 操作栏状态
        mVisible = true
    }

    private fun initSetContentViewPart2(){
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
     * 加载首选项
     */
    private fun initPreferences() {
        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        // 获取用户协议状态true为已签署，false为未签署
        mLicence = mSharedPreferences.getBoolean(
            mUtilsCompanion.licence,
            true
        )
        // 获取是否启用动态颜色
        mDynamicColor = mSharedPreferences.getBoolean(
            mUtilsCompanion.dynamicColor,
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        // 获取是否使用完整桌面模式
        mLibTaskBar = mSharedPreferences.getBoolean(
            mUtilsCompanion.libTaskbar,
            true
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    private fun initAppsList() {
        // 加载应用列表
        @Suppress("DEPRECATION")
        mAppsList = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
        )

        mAppsAdapter = object : BaseAdapter() {

            override fun getCount(): Int {
                return mAppsList.size
            }

            override fun getItem(position: Int): Any {
                return mAppsList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                return LinearLayoutCompat(
                    this@MainActivity
                ).apply {
                    layoutParams = LinearLayoutCompat.LayoutParams(
                        resources.getDimension(R.dimen.app_width).toInt(),
                        resources.getDimension(R.dimen.app_height).toInt()
                    )
                    orientation = LinearLayoutCompat.VERTICAL
                    addView(
                        AppCompatImageView(
                            this@MainActivity
                        ).apply {
                            setImageDrawable(
                                mAppsList[position].activityInfo.loadIcon(
                                    packageManager
                                )
                            )
                            setPadding(resources.getDimension(R.dimen.icon_padding).toInt())
                        },
                        LinearLayoutCompat.LayoutParams(
                            resources.getDimension(R.dimen.icon_size).toInt(),
                            resources.getDimension(R.dimen.icon_size).toInt()
                        ).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    )
                    addView(
                        AppCompatTextView(
                            this@MainActivity
                        ).apply {
                            gravity = Gravity.CENTER
                            text = mAppsList[position].loadLabel(packageManager)
                        },
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        )
                    )
                }
            }
        }

        //设置适配器。
        mAppsGridView.apply {
            adapter = mAppsAdapter
        }
    }

    private fun receiveSystemCast() {
        mIntentFilter = IntentFilter()
        mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED")
        mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED")
        mIntentFilter.addDataScheme("package")

        mBroadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(
                context: Context?,
                intent: Intent?
            ) {
                initAppsList()
            }
        }

        registerReceiver(mBroadcastReceiver, mIntentFilter)
    }

    private fun initCreate() {


//        // 判断是否同意用户协议
//        if (mLicence) {
//            log("已同意用户协议")
//            // 判断Shizuku是否安装
//            if (AppUtils.isAppInstalled(Packages.shizuku)) {
//                log("Shizuku已安装")
//                // 判断Termux是否安装
//                if (AppUtils.isAppInstalled(Packages.termux)) {
//                    log("Termux已安装")
//                    // 检查读取内部存储/读取照片权限
//                    if (mUtils.checkReadMedia(activity = this@MainActivity)) {
//                        log("已授予读取权限")
//                        // 尝试检查shizuku权限
//                        try {
//                            if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
//                                log("已授予shizuku权限")
//                                // 检查并授予签名欺骗权限（非必要）
//                                if (!mUtils.checkSignature(context = this@MainActivity)) {
//                                    mUtils.grantSignature(
//                                        activity = this@MainActivity,
//                                        code = Codes.requestCodeFakePackageSignature
//                                    )
//                                }
//                                // 检查设备是否支持谷歌基础服务
//                                checkGooglePlayServices {
//                                    // 当前设备不支持谷歌基础服务所要做的处理
//                                }
//
//
//                                log("ojbk")
//                            } else {
//                                // 申请shizuku权限
//                                mUtils.grantShizuku(Codes.requestCodeShizuku)
//                            }
//                        } catch (e: Exception) {
//                            // 弹出错误对话框
//                            // shizuku未激活
//                            log("Shizuku寄了")
//                            log(Log.getStackTraceString(e))
//                        }
//                    } else {
//                        // 申请读取内部存储/读取照片权限
//                        mUtils.grantReadMedia(this@MainActivity, Codes.requestCodeReadExternalStorage)
//                    }
//                } else {
//                    // 安装Termux
//                    log("安装Termux")
//                }
//            } else {
//                // 安装Shizuku
//                log("安装Shizuku")
//            }
//        } else {
//            // 签署用户协议
//            log("签署用户协议")
//        }

        // 签署用户协议
        // 检查shizuku termux是否安装
        // 判断Shizuku是否安装
        // 判断Termux是否安装
        // 判断设置是否支持GMS
        // 检查读取内部存储权限
        // 检查Shizuku权限
        // 检查签名欺骗权限
        // 检查通知权限


    }

    /**
     * 加载用户界面
     */
    private fun initUi() {
        // 设置工具栏前景色
        mAppBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this@MainActivity)
        // 工具栏
        mToolbar.setNavigationOnClickListener {

        }

        mTipProgressBar.setOnClickListener {
            toggle()
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
                // 获取上下文
                val context = LocalContext.current
                // 获取ComposeView
                val view = LocalView.current
                // 初始化系统界面控制工具
                val systemUiController = rememberSystemUiController()
                // 获取窗口
                val window = (view.context as Activity).window
                // 颜色
                val colorScheme = when {
                    mDynamicColor -> {
                        if (darkTheme) {
                            dynamicDarkColorScheme(context)
                        } else {
                            dynamicLightColorScheme(context)
                        }
                    }
                    darkTheme -> darkColorScheme
                    else -> lightColorScheme
                }
                // 系统界面设置
                if (!view.isInEditMode) {
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = !darkTheme
                        )
                        WindowCompat.getInsetsController(
                            window,
                            view
                        )
                    }
                }
                // 主题和内容
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = typography,
                ) {
                    MainComposable()
                }
            }
        }
    }

    @Composable
    private fun MainComposable() {
        ActivityMain(
            androidVersion = mUtils.getAndroidVersion(),
            shizukuVersion = "",
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
            NavigationOnClick = { /*TODO*/ },
            MenuOnClick = { /*TODO*/ },
            SearchOnClick = { /*TODO*/ },
            SheetOnClick = { /*TODO*/ },
            AppsOnClick = { /*TODO*/ }) {

        }
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
            // 如果已卸载但未刷新跳转由于市场防止程序崩溃
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

    private fun initServices() {
        val intent = Intent(this@MainActivity, MainService().javaClass)
        startService(intent)
        bindService(intent, mUtils.mConnection, Context.BIND_AUTO_CREATE)
    }


    private fun onRequestPermissionsResults(requestCode: Int, grantResult: Int) {
        if (
            grantResult == PackageManager.PERMISSION_GRANTED
        ) {
            if (
                requestCode == Codes.requestCodeShizuku
            ) {

            } else {
                // 申请手动授权
                MessageDialog.show("申请授权失败", "您已拒绝授予Shizuku权限，此权限是应用运行的必须权限，请您手动授予此权限。")
                    .setOkButton("授权") { _, _ ->
                        // 打开Shizuku手动授权
                        false
                    }
                    .setCancelButton("拒绝") { _, _ ->
                        // 结束应用进程
                        mUtils.killAppProcess(
                            context = this@MainActivity
                        )
                        false
                    }
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

    @Suppress("DEPRECATION")
    private fun show() {
        mVisible = true
        mHandler.postDelayed(
            showPart2Runnable,
            mUtilsCompanion.uiAnimatorDelay.toLong()
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
}