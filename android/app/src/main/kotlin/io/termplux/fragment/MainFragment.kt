package io.termplux.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.compose.rememberNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.window.layout.FoldingFeature
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.dialogx.dialogs.PopTip
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.activity.TermPluxActivity
import io.termplux.adapter.AppsAdapter
import io.termplux.adapter.ViewPager2Adapter
import io.termplux.model.AppsModel
import io.termplux.receiver.AppsReceiver
import io.termplux.ui.ActivityMain
import io.termplux.ui.window.ContentType
import io.termplux.ui.window.DevicePosture
import io.termplux.ui.window.NavigationType
import io.termplux.ui.window.isBookPosture
import io.termplux.ui.window.isSeparating
import io.termplux.utils.ZoomOutPageTransformer
import kotlinx.coroutines.Runnable
import java.util.ArrayList
import kotlin.math.hypot

class MainFragment : FlutterBoostFragment(), Runnable {

    private lateinit var channel: MethodChannel
    private lateinit var mContext: Context

    // View
    private lateinit var mFlutterView: View
    private lateinit var mComposeView: ComposeView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSettingsView: FragmentContainerView
    private lateinit var mViewPager: ViewPager2
    private lateinit var mSplashLogo: AppCompatImageView

    private var settingsFragment: SettingsFragment? = null

    private lateinit var appReceiver: BroadcastReceiver
    private lateinit var intentFilter: IntentFilter

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val messenger = flutterEngine.dartExecutor.binaryMessenger
        channel = MethodChannel(messenger, "termplux_channel")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)?.let { parent ->
            mFlutterView = parent
        }
        mComposeView = ComposeView(mContext).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
        mRecyclerView = RecyclerView(mContext).apply {
            layoutManager = GridLayoutManager(
                mContext,
                4,
                RecyclerView.VERTICAL,
                false
            )
        }
        mSettingsView = FragmentContainerView(mContext).apply {
            id = R.id.settings_containerssss
            fitsSystemWindows = true
        }





//        val mFragmentManager: FragmentManager = childFragmentManager
//        settingsFragment = mFragmentManager.findFragmentByTag(tagSettingsFragment) as SettingsFragment?
//        if (settingsFragment == null) {
//            val newSettingsFragment = SettingsFragment()
//            mFragmentManager.commit(
//                allowStateLoss = false,
//                body = {
//                    settingsFragment = newSettingsFragment
//                    add(
//                        mSettingsView.id,
//                        newSettingsFragment,
//                        tagSettingsFragment
//                    )
//                }
//            )
//        }




        mSplashLogo = AppCompatImageView(mContext).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.custom_termplux_24
                )
            )
        }
        val a = ViewPager2Adapter(
            flutterView = mFlutterView,
            composeView = mComposeView,
            recyclerView = mRecyclerView,
            fragmentContainerView = mSettingsView
        )
        mViewPager = ViewPager2(mContext).apply {
            visibility = View.INVISIBLE
            post(this@MainFragment)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
            setPageTransformer(
                ZoomOutPageTransformer()
            )
            adapter = a
            offscreenPageLimit = a.itemCount
        }



        return FrameLayout(mContext).apply {
            background = ContextCompat.getDrawable(
                mContext,
                R.drawable.custom_wallpaper_24
            )
            addView(
                mViewPager,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                mSplashLogo,
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
            x = mViewPager.width.toFloat(),
            y = mViewPager.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                mViewPager,
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
                mViewPager.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)











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
        mContext.registerReceiver(appReceiver, intentFilter)













        setContent {
            channel.setMethodCallHandler { call, res ->
                when (call.method) {
                    "pager" -> {
                        //mViewPager2.currentItem = mViewPager2.currentItem + 1
                        PopTip.show("6")
                        res.success("success")
                    }

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
                true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) dynamicDarkColorScheme(
                        context = mContext
                    ) else dynamicLightColorScheme(
                        context = mContext
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
                    androidVersion = "13",
                    shizukuVersion = "13",
                    current = { item ->

                    },
                    toggle = {
                        //toggle()
                    }
                )
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        mContext.unregisterReceiver(appReceiver)
        mViewPager.removeCallbacks(this@MainFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsFragment = null
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
        for (resolveInfo in mContext.packageManager.queryIntentActivities(
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

    private fun setContent(content: @Composable () -> Unit) {
        mComposeView.apply {
            setContent {
                content()
            }
        }
    }

    companion object {

        private const val tagSettingsFragment: String = "settings_fragment"

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