package io.termplux.basic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.flutter.embedding.android.FlutterFragment
import io.termplux.basic.fragment.*

class ContentAdapter constructor(
    activity: FragmentActivity,
    flutter: FlutterFragment,
    viewPager: ViewPager2,
    navigation: (String) -> Unit
) : FragmentStateAdapter(
    activity
) {

    /** ViewPager2实例 */
    private var mViewPager: ViewPager2

    /** FlutterFragment */
    private var mFlutter: FlutterFragment

    /** Compose NavHost 导航 */
    private var mNavigation: (String) -> Unit

    private lateinit var mHome: HomeFragment

    private lateinit var mLauncher: LauncherFragment

    /** 控制中心页面 */
    private lateinit var mNavFrag: NavigationFragment

    /** 设置页面 */
    private lateinit var mSettings: SettingsFragment

    /** 错误提示页面 */
    private lateinit var mError: ErrorFragment

    init {
        // 初始化变量
        // 传入ViewPager2
        mViewPager = viewPager
        // 传入FlutterFragment
        mFlutter = flutter
        // 传入导航函数
        mNavigation = navigation
    }

    /**
     * 返回页面数量
     */
    override fun getItemCount(): Int = intArrayOf(home, launcher, nav, settings).size

    /**
     * 返回Fragment页面
     */
    override fun createFragment(position: Int): Fragment {
        initFragment()
        return when (position) {
            home -> mHome
            launcher -> mLauncher
            nav -> mNavFrag
            settings -> mSettings
            else -> mError
        }
    }

    /**
     * 加载Fragment
     */
    private fun initFragment() {
        mHome = HomeFragment.newInstance(
            flutterFragment = mFlutter
        )

        // 桌面
        mLauncher = LauncherFragment.newInstance(
            viewPager = mViewPager,
            navigation = mNavigation
        )

        // 导航
        mNavFrag = NavigationFragment.newInstance(
            viewPager = mViewPager,
            navigation = mNavigation
        )

        // 设置
        mSettings = SettingsFragment.newInstance(
            navigation = mNavigation
        )

        // 错误提示
        mError = ErrorFragment.newInstance(
            viewPager = mViewPager
        )
    }

    companion object {

        fun newInstance(
            activity: FragmentActivity,
            flutter: FlutterFragment,
            viewPager: ViewPager2,
            navigation: (String) -> Unit
        ): ContentAdapter{
            return ContentAdapter(
                activity = activity,
                flutter = flutter,
                viewPager = viewPager,
                navigation = navigation
            )
        }
        // 页面代码，从0开始，以此类推

        // 主页
        const val home: Int = 0

        // 桌面
        const val launcher: Int = 1

        // 控制中心
        const val nav: Int = 2

        // 设置
        const val settings: Int = 3
    }
}