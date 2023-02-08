package io.termplux.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.adapter.MainAdapter
import kotlinx.coroutines.Runnable
import kotlin.math.hypot


class MainFragment(
    private var sectionNumber: Int,
    private val viewPager: ViewPager2
) : Fragment(), Runnable {

    /** 用于控件的上下文 */
    private lateinit var viewContext: Context

    /** 用于代码的上下文 */
    private lateinit var thisContext: Context

    private val mHandler = Handler(
        Looper.myLooper()!!
    )

    private val mShowRunnable = Runnable {
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    private var mVisible: Boolean = false

    private val mHideRunnable = Runnable {
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

    /** 首选项 */
    private lateinit var mSharedPreferences: SharedPreferences

    /** 用户协议状态 */
    private var mLicence: Boolean by mutableStateOf(true)

    /** 是否使用动态颜色 */
    private var mDynamicColor: Boolean by mutableStateOf(true)

    /** 是否使用完整桌面 */
    private var mLibTaskBar: Boolean by mutableStateOf(true)

    /** 应用运行模式 */
    private var mMode: Int by mutableStateOf(0)

    private lateinit var mSplashProgressBar: ProgressBar
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var mHomeComposeView: ComposeView
    private lateinit var mBackgroundImageView: AppCompatImageView
    private lateinit var mLauncherRefresh: SwipeRefreshLayout
    private lateinit var mLauncherGridView: GridView
    private lateinit var mLauncherAction: FloatingActionButton
    private lateinit var mSettingsContainerView: FragmentContainerView
    private lateinit var mErrorTextView: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取上下文
        viewContext = requireActivity()
        thisContext = requireActivity()


        // 获取首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(thisContext)
        // 获取用户协议状态true为已签署，false为未签署
        mLicence = mSharedPreferences.getBoolean("licence", true)
        // 获取是否启用动态颜色
        mDynamicColor = mSharedPreferences.getBoolean("dynamicColor", true)
        // 获取是否使用完整桌面模式
        mLibTaskBar = mSharedPreferences.getBoolean("libTaskbar", true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVisible = true
        return when (sectionNumber) {
            splash -> splashContent()
            home -> homeContent()
            manager -> managerContent()
            launcher -> launcherContent()
            settings -> settingsContent()
            else -> errorContent()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (sectionNumber) {
            splash -> splashCreated()
            home -> homeCreated()
            manager -> managerCreated()
            launcher -> launcherCreated()
            settings -> settingsCreated()
            else -> errorCreated()
        }
    }

    override fun run() {
        if (sectionNumber == splash) {
            val cx = mSplashLogo.x + mSplashLogo.width / 2f
            val cy = mSplashLogo.y + mSplashLogo.height / 2f
            val startRadius = hypot(
                x = mSplashLogo.width.toFloat(),
                y = mSplashLogo.height.toFloat()
            )
            val endRadius = hypot(
                x = mSplashProgressBar.width.toFloat(),
                y = mSplashProgressBar.height.toFloat()
            )
            val circularAnim = ViewAnimationUtils
                .createCircularReveal(
                    mSplashProgressBar,
                    cx.toInt(),
                    cy.toInt(),
                    startRadius,
                    endRadius
                )
                .setDuration(800)
            mSplashLogo.animate()
                .alpha(0f)
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(
                    600
                )
                .withEndAction {
                    mSplashLogo.visibility = View.GONE
                }
                .withStartAction {
                    mSplashProgressBar.visibility = View.VISIBLE
                    circularAnim.start()
                    //current(1)
                }
                .start()
        }
    }

    override fun onResume() {
        super.onResume()
        delayedHide(100)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (sectionNumber == 0) {
            mSplashProgressBar.removeCallbacks(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun splashContent(): View {

        // 初始化进度条
        mSplashProgressBar = ProgressBar(viewContext)

        // 初始化屏闪图标
        mSplashLogo = AppCompatImageView(viewContext).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    viewContext,
                    R.drawable.ic_baseline_termplux_24
                )
            )
        }

        // 返回布局
        return FrameLayout(viewContext).apply {
            fitsSystemWindows = true
            setOnTouchListener(
                delayHideTouchListener
            )
            addView(
                mSplashProgressBar,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
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

    private fun homeContent(): View {
        // 初始化ComposeView
        mHomeComposeView = ComposeView(viewContext).apply {
            setOnTouchListener(
                delayHideTouchListener
            )
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }

        // 返回ComposeView
        return mHomeComposeView
    }

    private fun managerContent(): View{


        return TextView(viewContext).apply {
            text = "sbsbsbsbsbsbsbsb"
        }
    }

    private fun launcherContent(): View {
        // 初始化用于显示背景图片的AppCompatImageView
        mBackgroundImageView = AppCompatImageView(viewContext).apply {
            scaleType = ImageView.ScaleType.FIT_XY
        }

        mLauncherRefresh = SwipeRefreshLayout(viewContext)

        // 初始化用于显示应用的GridView
        mLauncherGridView = GridView(viewContext).apply {
            numColumns = GridView.AUTO_FIT
            columnWidth = context.resources.getDimension(R.dimen.app_width).toInt()
        }

        // 返回布局
        return ConstraintLayout(viewContext).apply {
            addView(
                mBackgroundImageView,
                ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                LinearLayoutCompat(viewContext).apply {
                    fitsSystemWindows = true
                    orientation = LinearLayoutCompat.VERTICAL
                    addView(
                        TextClock(viewContext).apply {
                            format12Hour = "hh:mm"
                            format24Hour = "HH:mm"
                            textSize = 40F
                            gravity = Gravity.CENTER
                        },
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        )
                    )
                    addView(
                        TextClock(viewContext).apply {
                            format12Hour = "yyyy/MM/dd E"
                            format24Hour = "yyyy/MM/dd E"
                            textSize = 16F
                            gravity = Gravity.CENTER
                        },
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        )
                    )
                    addView(
                        mLauncherRefresh.apply {
                            addView(
                                mLauncherGridView,
                                LinearLayoutCompat.LayoutParams(
                                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                                )
                            )
                        },
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT
                        )
                    )
                },
                ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun settingsContent(): View {

        mSettingsContainerView = FragmentContainerView(
            viewContext
        ).apply {
            id = R.id.settings_container
        }

        return FrameLayout(viewContext).apply {
            fitsSystemWindows = true
            addView(
                mSettingsContainerView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun errorContent(): View {
        mErrorTextView = AppCompatTextView(viewContext).apply {
            fitsSystemWindows = true
            gravity = Gravity.CENTER
        }
        return mErrorTextView
    }

    private fun splashCreated() {
        mSplashProgressBar.visibility = View.INVISIBLE
        mSplashProgressBar.postDelayed(this, 200)
        mSplashProgressBar.setOnClickListener {
            toggle()
        }
    }

    private fun homeCreated() {
        mHomeComposeView.apply {
        }
    }

    private fun managerCreated(){

    }

    private fun launcherCreated() {

        // 加载可从桌面启动的APP并添加到列表
        val apps: List<ResolveInfo> = thisContext.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
        )

        // 设置壁纸
        mBackgroundImageView.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    thisContext,
                    R.drawable.background
                )
            )
        }

        mLauncherRefresh.apply {
            setOnRefreshListener {
                launcherCreated()
                mLauncherRefresh.isRefreshing = false
            }
        }

        mLauncherGridView.apply {

            //设置默认适配器。
            adapter = MainAdapter(
                appsList = apps,
                context = thisContext
            )

            // 应用长按事件
            setOnItemLongClickListener { _, view, position, _ ->
                // 获取目标应用包名
                val packageName = apps[position].activityInfo.packageName
                // 获取目标应用类名
                val className = apps[position].activityInfo.name
                // 初始化弹出菜单
                val popupMenu = PopupMenu(thisContext, view)
                // 加载菜单
                popupMenu.inflate(R.menu.apps_more_menu)
                // 点击事件
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        // 打开应用
                        R.id.open -> {
                            // 判断目标应用包名是否与自身相符
                            if (packageName != BuildConfig.APPLICATION_ID){
                                try {
                                    // 启动目标应用
                                    val intent = Intent()
                                    intent.component = ComponentName(packageName, className)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                } catch (e: Exception){
                                    // 如果已卸载但未刷新跳转由于市场防止程序崩溃
                                    openApplicationMarket(packageName = packageName)
                                }
                            } else {
                                // 跳转主页
                                current(sectionNumber = home)
                            }
                        }
                        // 跳转应用信息界面
                        R.id.info -> {
                            try {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.data = Uri.parse("package:$packageName")
                                startActivity(intent)
                            } catch (e: Exception){
                                openApplicationMarket(packageName = packageName)
                            }
                        }
                        // 卸载应用
                        R.id.delete -> {
                            try {
                                if (!isSystemApplication(packageName)){
                                    if (packageName != BuildConfig.APPLICATION_ID){
                                        val intent = Intent(Intent.ACTION_DELETE)
                                        intent.data = Uri.parse("package:$packageName")
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(thisContext, "你最好去设置进行卸载", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(thisContext, "无法卸载系统内置应用", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception){
                                openApplicationMarket(packageName = packageName)
                            }
                        }
                    }
                    true
                }
                popupMenu.show()
                // 劫持点按事件
                true
            }

            // 应用点击事件
            setOnItemClickListener { _, _, position, _ ->
                // 获取目标应用包名
                val packageName = apps[position].activityInfo.packageName
                // 获取目标应用类名
                val className = apps[position].activityInfo.name
                // 判断目标应用包名是否与自身相符
                if (packageName != BuildConfig.APPLICATION_ID){
                    try {
                        // 启动目标应用
                        val intent = Intent()
                        intent.component = ComponentName(packageName, className)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } catch (e: Exception){
                        // 如果已卸载但未刷新跳转由于市场防止程序崩溃
                        openApplicationMarket(packageName = packageName)
                    }
                } else {
                    // 跳转主页
                    current(sectionNumber = home)
                }
            }
        }
    }

    private fun settingsCreated() {
//        childFragmentManager
//            .beginTransaction()
//            .replace(
//                mSettingsContainerView.id,
//                SettingsFragment(
//                    viewPager = viewPager
//                )
//            )
//            .commit()
    }

    private fun errorCreated() {
        mErrorTextView.apply {
            text = "出戳辣~"
            textSize = 50F
            setTextColor(Color.RED)
        }
    }

    private fun mainLayout(){

    }

    private fun managerLayout(){

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
            val packageInfo = thisContext.packageManager.getPackageInfo(
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

    private fun current(sectionNumber: Int) {
        viewPager.setCurrentItem(sectionNumber, true)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
        mVisible = false
        mHandler.removeCallbacks(mShowRunnable)
    }

    private fun show() {
        mVisible = true
        mHandler.postDelayed(
            mShowRunnable, uiAnimationDelay.toLong()
        )
    }

    private fun delayedHide(delayMillis: Int) {
        mHandler.removeCallbacks(mHideRunnable)
        mHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }


    companion object {

        // 配置参数
        private const val autoHide = true
        private const val autoHideDelayMillis = 3000
        private const val uiAnimationDelay = 300

        // 页面代码
        private const val splash = 0
        private const val home = 1
        private const val manager = 2
        private const val launcher = 3
        private const val settings = 4

        // 加载页面
        @JvmStatic
        fun newInstance(
            sectionNumber: Int,
            viewPager: ViewPager2
        ): MainFragment {
            return MainFragment(
                sectionNumber = sectionNumber,
                viewPager = viewPager
            )
        }
    }
}