package io.termplux.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.internal.EdgeToEdgeUtils
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.termplux.adapter.MainAdapter
import io.termplux.ui.ActivityMain

class MainActivity : AppCompatActivity(), FlutterEngineConfigurator {

    private val purple80: Color = Color(0xFFD0BCFF)
    private val purpleGrey80: Color = Color(0xFFCCC2DC)
    private val pink80: Color = Color(0xFFEFB8C8)

    private val purple40: Color = Color(0xFF6650a4)
    private val purpleGrey40: Color = Color(0xFF625b71)
    private val pink40: Color = Color(0xFF7D5260)

    private val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )

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

    private lateinit var flutterFragment: FlutterFragment

    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initFlutter()


        viewPager2 = ViewPager2(this@MainActivity)
        val mainAdapter = MainAdapter(
            activity = this@MainActivity,
            flutter = flutterFragment,
            viewPager = viewPager2
        )
        viewPager2.apply {
            adapter = mainAdapter
            offscreenPageLimit = mainAdapter.itemCount
        }
        initContent()
        initSystemBar()
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    private fun initFlutter() {
        // 创建Flutter引擎缓存
        val flutterEngine = FlutterEngine(this@MainActivity)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(flutter_engine, flutterEngine)

        // 初始化FlutterFragment
        flutterFragment = FlutterFragment
            .withCachedEngine(flutter_engine)
            .shouldAttachEngineToActivity(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.transparent)
            .build()
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
    }

    private fun initContent(){
        setContent {
            // 获取系统是否为深色模式
            val darkTheme = isSystemInDarkTheme()
            // 初始化系统界面控制工具
            val systemUiController = rememberSystemUiController()
            val view = LocalView.current
            // 颜色
            val colorScheme: ColorScheme = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (darkTheme) {
                        dynamicDarkColorScheme(
                            context = this@MainActivity
                        )
                    } else {
                        dynamicLightColorScheme(
                            context = this@MainActivity
                        )
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
                ActivityMain(
                    viewPager = viewPager2,
                    onOptionsMenu = {

                    },
                    onToggle = {

                    },
                    androidVersion = "13",
                    shizukuVersion = "13",
                    infoApp = { packageName ->
                        AppUtils.launchAppDetailsSettings(packageName)
                    },
                    deleteApp = { packageName ->
                        AppUtils.uninstallApp(packageName)
                    },
                    targetAppVersionName = "",
                    dynamicColorChecked = true,
                    taskBarChecked = true,
                    onDynamicChecked = { value ->

                    },
                    onTaskBarChecked = { value ->

                    },
                    onTaskBarSettings = {
                        //taskbarSettings()
                    },
                    onSystemSettings = {
                        //systemSettings()
                    },
                    onDefaultLauncherSettings = {
                        //defaultLauncher()
                    },

                    onEasterEgg = {
                        //easterEgg()
                    },
                    onNotice = {
                        //licenseDialog()
                    },
                    onSource = {
                        //fullScreenWebView("https://github.com/TermpPlux/TermPlux-App")
                    },
                    onDevGitHub = {
                        //fullScreenWebView("https://github.com/wyq0918dev")
                    },
                    onDevTwitter = {
                        //fullScreenWebView("https://twitter.com/wyq0918dev")
                    },
                    onTeamGitHub = {
                        //fullScreenWebView("https://github.com/TermPlux")
                    }
                )
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = {
//                        AndroidView(
//                            factory = { context ->
//                                MaterialToolbar(context).apply {
//                                    setSupportActionBar(this@apply)
//                                    setNavigationIcon(R.drawable.baseline_arrow_back_24)
//                                    subtitle = "操你妈"
//                                    logo = ContextCompat.getDrawable(
//                                        context,
//                                        R.drawable.baseline_terminal_24
//                                    )
//                                    setNavigationOnClickListener {
//                                        finish()
//                                    }
//
//                                    supportActionBar?.hide()
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                ) { paddingValues ->
//                    AndroidView(
//                        factory = { context ->
//                            ViewPager2(context).apply {
//                                adapter = flutter
//                                offscreenPageLimit = flutter.itemCount
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues),
//                        update = { view ->
//
//                        }
//                    )
//                }
            }
        }
    }

    companion object {
        const val flutter_engine = "termplux_flutter"
    }
}