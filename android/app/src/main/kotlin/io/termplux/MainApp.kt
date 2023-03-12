package io.termplux

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.interfaces.OnBugReportListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.ScrollController
import com.kongzue.dialogx.style.IOSStyle
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.termplux.ui.navigation.*
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.io.File
import java.util.*

open class MainApp : BaseApp<MainApp>() {

    /** 首选项 */
    private lateinit var mSharedPreferences: SharedPreferences

    override fun init() {
        // 加载首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainApp)

        // 触发错误时调用
        setOnCrashListener(
            object : OnBugReportListener() {
                override fun onCrash(e: Exception, crashLogFile: File): Boolean {
                    if (AppManager.getInstance().activeActivity == null || !AppManager.getInstance().activeActivity.isActive) {
                        return false
                    }
                    try {
                        MessageDialog.show("Ops！发生了一次崩溃！", "您是否愿意帮助我们改进程序以修复此Bug？")
                            .setOkButton("愿意") { _, _ ->
                                toast("我真的会谢")
                                false
                            }
                            .setCancelButton("不了") { _, _ ->
                                toast("抱歉打扰了")
                                false
                            }
                    } catch (e: Exception) {
                        log(Log.getStackTraceString(e))
                    }
                    return false
                }
            }
        )
    }

    override fun initSDKs() {
        super.initSDKs()

        TermPlux.App.MainApplication.newInstance()

        // 初始化DialogX
        DialogX.init(this@MainApp)
        DialogX.globalStyle = IOSStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.autoShowInputKeyboard = true
        DialogX.onlyOnePopTip = false
        DialogX.cancelable = true
        DialogX.cancelableTipDialog = false
        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
        DialogX.autoRunOnUIThread = true
        DialogX.useHaptic = true

        // 初始化动态颜色
        if (mSharedPreferences.getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@MainApp)


        // 初始化任务栏
        Taskbar.setEnabled(
            this@MainApp,
            mSharedPreferences.getBoolean(
                "desktop",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    override fun initSDKInitialized() {
        super.initSDKInitialized()
        val msg = "SDK已加载完毕"
        try {
            PopTip.show(msg)
        } catch (e: Exception) {
            toast(msg)
        }
    }

    internal class TermPlux : MainApp() {

        class Abstract {

            abstract class TermPluxActivity : BaseActivity(), FlutterEngineConfigurator {

                override fun resetContentView(): View {
                    super.resetContentView()
                    initView()
                    return contentView()
                }

                override fun initViews() {

                }

                override fun initDatas(parameter: JumpParameter?) {

                }

                override fun setEvents() {

                }

                override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
                    configure(flutterEngine = flutterEngine)
                }

                override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

                }

                abstract fun initView()
                abstract fun contentView(): View

                abstract fun configure(flutterEngine: FlutterEngine)
            }
        }

        class App {

            class MainApplication : FlutterApplication() {

                private fun currentActivity() {
                    currentActivity = AppManager.getInstance().currentActivity()
                }

                companion object {
                    fun newInstance() {
                        return MainApplication().currentActivity()
                    }
                }
            }

            class MainActivity : Abstract.TermPluxActivity() {

                private lateinit var mComposeView: ComposeView

                override fun initView() {
                    mComposeView = ComposeView(context = this@MainActivity).apply {

                    }
                }

                override fun contentView(): View = mComposeView

                override fun configure(flutterEngine: FlutterEngine) {
                    TODO("Not yet implemented")
                }

            }

            class FlutterPreviewActivity: FlutterActivity(){

                override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
                    super.configureFlutterEngine(flutterEngine)
                }

                companion object{

                    init {
                        System.loadLibrary("termplux")
                    }
                }
            }

            class MainAdapter(
                activity: BaseActivity,
                flutter: FlutterFragment,
                viewPager: ViewPager2
            ) : FragmentStateAdapter(
                activity
            ) {

                private var mFlutter: FlutterFragment

                private val mError: ErrorFragment

                init {
                    mFlutter = flutter

                    mError = ErrorFragment.newInstance(
                        toggle = {

                        }
                    )
                }

                override fun getItemCount(): Int {
                    return 3
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        flutter -> mFlutter
                        else -> mError
                    }
                }

                companion object {
                    const val flutter: Int = 0
                }
            }

            class ErrorFragment(toggle: () -> Unit) : Fragment() {

                private lateinit var mErrorTipTextView: AppCompatTextView

                override fun onCreateView(
                    inflater: LayoutInflater,
                    container: ViewGroup?,
                    savedInstanceState: Bundle?
                ): View {
                    super.onCreateView(inflater, container, savedInstanceState)
                    mErrorTipTextView = AppCompatTextView(
                        requireActivity()
                    )
                    return mErrorTipTextView
                }


                override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                    super.onViewCreated(view, savedInstanceState)
                    mErrorTipTextView.apply {
                        gravity = Gravity.CENTER
                        setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.black
                            )
                        )
                        textSize = 50f
                        text = "ERROR!"
                    }
                }

                companion object {
                    fun newInstance(toggle: () -> Unit): ErrorFragment {
                        return ErrorFragment(toggle = toggle)
                    }
                }
            }
        }

        class Custom {

            sealed class Screen(
                val route: String,
                val imageVector: ImageVector,
                @StringRes val title: Int
            ) {

                object Navigate : Screen(
                    route = routeNavigation,
                    imageVector = Icons.TwoTone.Navigation,
                    title = R.string.menu_navigation
                )

                object Home : Screen(
                    route = routeHome,
                    imageVector = Icons.TwoTone.Home,
                    title = R.string.menu_home
                )

                object Apps : Screen(
                    route = routeLauncher,
                    imageVector = Icons.TwoTone.RocketLaunch,
                    title = R.string.menu_apps
                )

                object Dashboard : Screen(
                    route = routeDashboard,
                    imageVector = Icons.TwoTone.Dashboard,
                    title = R.string.menu_dashboard
                )

                object Manager : Screen(
                    route = routeManager,
                    imageVector = Icons.TwoTone.AppSettingsAlt,
                    title = R.string.menu_manager
                )

                object Flutter : Screen(
                    route = routeFlutter,
                    imageVector = Icons.TwoTone.FlutterDash,
                    title = R.string.menu_flutter
                )

                object Settings : Screen(
                    route = routeSettings,
                    imageVector = Icons.TwoTone.Settings,
                    title = R.string.menu_settings
                )

                object About : Screen(
                    route = routeAbout,
                    imageVector = Icons.TwoTone.Info,
                    title = R.string.menu_about
                )
            }

            class ScrollControllerWebView(context: Context) : WebView(context), ScrollController {

                private var lockScroll = false

                override fun isLockScroll(): Boolean {
                    return lockScroll
                }

                override fun lockScroll(lockScroll: Boolean) {
                    this.lockScroll = lockScroll
                }

                @SuppressLint("ClickableViewAccessibility")
                override fun onTouchEvent(event: MotionEvent): Boolean {
                    return if (lockScroll) false else super.onTouchEvent(event)
                }

                override fun getScrollDistance(): Int {
                    return scrollY
                }

                override fun isCanScroll(): Boolean {
                    return true
                }
            }

            class LinkNativeViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {

                override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
                    return LinkNativeView(context)
                }
            }

            class LinkNativeView(context: Context) : PlatformView {

                private var mContext: Context
                private lateinit var button: AppCompatButton

                init {
                    mContext = context
                }

                override fun getView(): AppCompatButton {

                    button = AppCompatButton(mContext)

                    button.text = "fuck"
                    button.setOnClickListener {
                        Toast.makeText(mContext, "oh ye", Toast.LENGTH_SHORT).show()
                    }
                    return button
                }

                override fun dispose() {

                }
            }
        }

        class Core {

            init {

            }

            fun getDayLunar(): String {
                val lunarCalender = ChineseCale()
                val calendar = Calendar.getInstance()

                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1
                val day = calendar[Calendar.DATE]

                val lunarAnimal = lunarCalender.animalsYear(year)
                val lunarGanZhi = lunarCalender.cyclical(year, month, day)
                val lunarString = lunarCalender.getLunarString(year, month, day)

                return "农历\t$lunarGanZhi\t$lunarAnimal\t$lunarString"
            }

            fun getCarrierName(context: Context): String {
                val tellMgr = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                return tellMgr.simOperatorName
            }



            companion object {

            }
        }

        class Utils {

        }

        class Library {

        }

        class Classes {

        }

        class Resource {

            class Navigation {

            }

            class Layout {

            }

            class Values {

            }
        }

    }


}