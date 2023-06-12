package io.termplux.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Space
import androidx.core.view.GravityCompat
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterView
import io.termplux.utils.FlutterViewReturn

class FlutterFragment : FlutterBoostFragment() {

//
//    private lateinit var mBottomNavigationView: BottomNavigationView
//    private lateinit var mTabLayout: TabLayout
//    private lateinit var mViewPager2: ViewPager2
//
//    private lateinit var mComposeView: ComposeView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FrameLayout(context).apply {
        addView(
            ProgressBar(context),
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
    }.also {
        when (activity) {
            is FlutterViewReturn -> (activity as FlutterViewReturn).apply {
                returnFlutterView(
                    flutterView = findFlutterView(
                        view = super.onCreateView(
                            inflater,
                            container,
                            savedInstanceState
                        )
                    )
                )
            }
        }
    }

    private fun findFlutterView(view: View?): FlutterView? {
        when {
            (view is FlutterView) -> return view
            (view is ViewGroup) -> {
                for (i in 0 until view.childCount) {
                    findFlutterView(view.getChildAt(i))?.let {
                        return it
                    }
                }
            }
        }
        return null
    }


//    override fun onCreateVisew(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): ComposeView = ComposeView(
//        context = context
//    ).apply {
//        setViewCompositionStrategy(
//            strategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
//        )
//    }.also { compose ->
//        mComposeView = compose
//    }.apply {
//
//
//        mAppBarLayout = AppBarLayout(
//            context
//        ).apply {
//            fitsSystemWindows = true
//            addView(
//                MaterialToolbar(
//                    context
//                ).also { toolbar ->
//                    mActivity.setSupportActionBar(toolbar)
//                    mMaterialToolbar = toolbar
//                },
//                AppBarLayout.LayoutParams(
//                    AppBarLayout.LayoutParams.MATCH_PARENT,
//                    AppBarLayout.LayoutParams.WRAP_CONTENT
//                )
//            )
//        }.also { appBar ->
//            appBar.isLiftOnScroll = true
//            appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
//                context
//            )
//        }
//
//        mBottomNavigationView = BottomNavigationView(context).apply {
//            inflateMenu(R.menu.navigation)
//        }
//        mTabLayout = TabLayout(context)
//
//        ViewPager2(context).apply {
//            // 初始值为否，会在动画结束后恢复是，目的是防止动画加载过程中用户操作导致错误
//            isUserInputEnabled = false
//            // 设置方向为水平
//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            // 设置切换动画
//            setPageTransformer(PageTransformerUtils())
//            // 设置适配器
//            adapter = PagerAdapter(
//                rootLayout = FrameLayout(context).apply {
//                    addView(
//                        findFlutterView(
//                            view = super.onCreateView(
//                                inflater,
//                                container,
//                                savedInstanceState
//                            )
//                        )?.also {
//                            mFlutterView = it
//                            (it.parent as ViewGroup).removeView(it)
//                            it.apply {
//                                setOnTouchListener(delayHideTouchListener)
//                                visibility = View.INVISIBLE
//                                post(mFragmentContext as Runnable)
//                            }
//                        },
//                        FrameLayout.LayoutParams(
//                            FrameLayout.LayoutParams.MATCH_PARENT,
//                            FrameLayout.LayoutParams.MATCH_PARENT
//                        )
//                    )
//                    addView(
//                        AppCompatImageView(context).apply {
//                            setImageDrawable(
//                                ContextCompat.getDrawable(
//                                    requireActivity(),
//                                    R.drawable.custom_termplux_24
//                                )
//                            )
//                        }.also { logo ->
//                            mSplashLogo = logo
//                        },
//                        FrameLayout.LayoutParams(
//                            FrameLayout.LayoutParams.WRAP_CONTENT,
//                            FrameLayout.LayoutParams.WRAP_CONTENT,
//                            Gravity.CENTER
//                        )
//                    )
//                },
//                composeView = ComposeView(context),
//                refreshLayout = SwipeRefreshLayout(context).apply {
//                    addView(
//                        RecyclerView(context).apply {
//                            layoutManager = GridLayoutManager(
//                                context,
//                                4,
//                                RecyclerView.VERTICAL,
//                                false
//                            )
//                        }.also { apps ->
//                            mRecyclerView = apps
//                        },
//                        ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT
//                        )
//                    )
//                    setOnRefreshListener {
//                        // 执行下滑刷新事件
//                        PopTip.show("下拉刷新")
//
//                        isRefreshing = false
//                    }
//                },
//                viewPager = ViewPager2(context).apply {
//                    // 此页面仅展示设置，禁止用户滑动
//                    isUserInputEnabled = false
//                    // 设置方向为水平
//                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
//                    // 设置适配器
//                    adapter = SettingsAdapter(
//                        mFragmentContext,
//                        settings = {}
//                    )
//                }
//            )
//            // 预加载全部页面
//            offscreenPageLimit = PagerAdapter.count
//        }.also { pager ->
//            mViewPager2 = pager
//        }
//
//
//
//
//    }


//    private fun mediator(navController: NavHostController) {
//        MediatorUtils(
//            bottomNavigation = mBottomNavigationView,
//            tabLayout = mTabLayout,
//            viewPager = mViewPager2,
//            home = {
//                navController.navigate(
//                    route = Screen.Home.route
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
//        ) { page, tab, position ->
//            page.apply {
////                orientation = when (navigationType) {
////                    NavigationType.BottomNavigation -> ViewPager2.ORIENTATION_HORIZONTAL
////                    NavigationType.NavigationRail -> ViewPager2.ORIENTATION_VERTICAL
////                    NavigationType.PermanentNavigationDrawer -> ViewPager2.ORIENTATION_VERTICAL
////                }
////                isUserInputEnabled = navigationType != NavigationType.NavigationRail
//            }
//            tab.apply {
//                text = arrayOf(
//                    getString(R.string.menu_launcher),
//                    getString(R.string.menu_launcher),
//                    getString(R.string.menu_apps),
//                    getString(R.string.menu_settings)
//                )[position]
//            }
//        }.attach()
//    }
//
//    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
//    private fun setContent() {
//        mComposeView.apply {
//            setContent {
//
//
//                // 获取根View
//                val hostView = LocalView.current
//                val window = (hostView.context as Activity).window
//
//                val scope = rememberCoroutineScope()
//                val drawerState = rememberDrawerState(
//                    initialValue = DrawerValue.Closed
//                )
//
//                // 导航控制器实例
//                val navController = rememberNavController()
//
//
//                val windowSize = calculateWindowSizeClass(
//                    activity = requireActivity()
//                )
//                val displayFeatures = calculateDisplayFeatures(
//                    activity = requireActivity()
//                )
//
//
//                // 系统界面控制器实例
//                val systemUiController = rememberSystemUiController()
//                // 判断系统是否处于深色模式
//                val darkTheme = isSystemInDarkTheme()
//                // 配色方案
//                val colorScheme = when {
//                    true -> {
//                        if (darkTheme) dynamicDarkColorScheme(
//                            context = context
//                        ) else dynamicLightColorScheme(
//                            context = context
//                        )
//                    }
//
//                    darkTheme -> DarkColorScheme
//                    else -> LightColorScheme
//                }
////                adapter(navController = navController)
//                mediator(navController = navController)
//                // 主机控件触摸事件
//                //touch(hostView = hostView)
//                // 绑定操作栏与导航抽屉
////            bindingDrawer(
////                navigationType = navigationType,
////                open = {
////                    scope.launch {
////                        drawerState.open()
////                    }
////                }
////            )
//
//                // 设置系统界面样式
//                if (!hostView.isInEditMode) {
//                    SideEffect {
//                        systemUiController.setSystemBarsColor(
//                            color = Color.Transparent,
//                            darkIcons = !darkTheme
//                        )
//                        WindowCompat.getInsetsController(
//                            window,
//                            hostView
//                        )
//                    }
//                }
//                // 设置页面样式和内容
//                MaterialTheme(
//                    colorScheme = colorScheme,
//                    typography = Typography,
//                ) {
//                    ActivityMain(
//                        navController = navController,
//                        drawerState = drawerState,
//                        windowSize = windowSize,
//                        displayFeatures = displayFeatures,
//                        topBar = {
////                            AndroidView(
////                                factory = {
////                                    mAppBarLayout
////                                },
////                                modifier = it
////                            )
//                        },
//                        pager = {
//                            AndroidView(
//                                factory = {
//                                    mViewPager2
//                                },
//                                modifier = it
//                            )
//                        },
//                        navBar = {
//                            AndroidView(
//                                factory = {
//                                    mBottomNavigationView
//                                },
//                                modifier = it
//                            )
//                        },
//                        tabRow = {
////                        AndroidView(
////                            factory = {
////                                mTabLayout
////                            },
////                            modifier = it
////                        )
//                        },
//                        optionsMenu = {
//                            // optionsMenu()
//                        },
//                        androidVersion = "13",
//                        shizukuVersion = "13",
//                        current = { item ->
//
//                        },
//                        toggle = {
//                            //toggle()
//                        }
//                    )
//                }
//            }
//        }
//    }

//    companion object {
//
//        // 浅色模式配色
//        private val Purple80 = Color(0xFFD0BCFF)
//        private val PurpleGrey80 = Color(0xFFCCC2DC)
//        private val Pink80 = Color(0xFFEFB8C8)
//
//        // 深色模式配色
//        private val Purple40 = Color(0xFF6650a4)
//        private val PurpleGrey40 = Color(0xFF625b71)
//        private val Pink40 = Color(0xFF7D5260)
//
//        // 样式
//        val Typography = Typography(
//            bodyLarge = TextStyle(
//                fontFamily = FontFamily.Default,
//                fontWeight = FontWeight.Normal,
//                fontSize = 16.sp,
//                lineHeight = 24.sp,
//                letterSpacing = 0.5.sp
//            )
//        )
//
//        // 深色模式
//        val DarkColorScheme = darkColorScheme(
//            primary = Purple80,
//            secondary = PurpleGrey80,
//            tertiary = Pink80
//        )
//
//        // 浅色模式
//        val LightColorScheme = lightColorScheme(
//            primary = Purple40,
//            secondary = PurpleGrey40,
//            tertiary = Pink40
//        )
//
//
//    }
}