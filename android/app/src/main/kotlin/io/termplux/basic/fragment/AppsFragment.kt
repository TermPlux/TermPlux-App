package io.termplux.basic.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.basic.adapter.AppsAdapter
import io.termplux.basic.receiver.AppsReceiver
import io.termplux.model.AppsModel
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.math.hypot

class AppsFragment constructor(
    viewPager: ViewPager2,
    navigation: (String) -> Unit
) : Fragment(), Runnable {

    /** ViewPager2实例 */
    private var mViewPager: ViewPager2

    /** Compose导航函数 */
    private var mNavigation: (String) -> Unit

    private lateinit var mContext: Context

    private lateinit var recyclerView: RecyclerView
    private lateinit var mSplashLogo: AppCompatImageView
    private lateinit var appReceiver: BroadcastReceiver

    init {
        mViewPager = viewPager
        mNavigation = navigation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取上下文
        mContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 创建用于显示启动器列表的RecyclerView
        recyclerView = RecyclerView(
            mContext
        ).apply {
            layoutManager = GridLayoutManager(
                mContext,
                4,
                RecyclerView.VERTICAL,
                false
            )
        }

        mSplashLogo = AppCompatImageView(
            mContext
        ).apply {
            scaleType = ImageView.ScaleType.CENTER
            setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.custom_termplux_24
                )
            )
        }

        // 返回启动器
        return FrameLayout(
            mContext
        ).apply {
            // 设置背景
            background = ContextCompat.getDrawable(
                mContext,
                R.drawable.custom_wallpaper_24
            )
            addView(
                recyclerView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            addView(
                mSplashLogo,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
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
            x = recyclerView.width.toFloat(),
            y = recyclerView.height.toFloat()
        )
        val circularAnim = ViewAnimationUtils
            .createCircularReveal(
                recyclerView,
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
                recyclerView.visibility = View.VISIBLE
                circularAnim.start()
            }
            .start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.visibility = View.INVISIBLE
        recyclerView.post(this@AppsFragment)
        // 加载应用列表
        loadApp()

        // 意图过滤器
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")

        // 用于刷新应用列表的广播接收器
        appReceiver = AppsReceiver(
            refresh = {
                loadApp()
            }
        )

        // 注册广播接收器
        mContext.registerReceiver(appReceiver, intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.removeCallbacks(this@AppsFragment)
        // 注销广播接收器
        mContext.unregisterReceiver(appReceiver)
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
            if (packageFilter(pkg = pkg)) {
                applicationList.add(
                    AppsModel(pkgName = pkg)
                )
              //  Log.i("Launcher", AppsModel(pkgName = pkg).toString())
            }
        }

        // 设置适配器
        recyclerView.adapter = AppsAdapter.newInstance(
            applicationList = applicationList,
            viewPager = mViewPager
        )
    }

    /**
     * 包过滤器 - 过滤不必要应用
     */
    private fun packageFilter(pkg: String): Boolean {
        return pkg != BuildConfig.APPLICATION_ID
    }

    companion object {

        // 初始化函数
        fun newInstance(
            viewPager: ViewPager2,
            navigation: (String) -> Unit
        ): AppsFragment {
            return AppsFragment(
                viewPager = viewPager,
                navigation = navigation
            )
        }

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800
    }
}