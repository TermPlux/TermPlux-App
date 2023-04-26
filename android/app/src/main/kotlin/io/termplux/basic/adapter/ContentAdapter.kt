package io.termplux.basic.adapter

import androidx.compose.material3.DrawerState
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import io.flutter.embedding.android.FlutterFragment
import io.termplux.basic.fragment.*

class ContentAdapter constructor(
    activity: FragmentActivity,
    flutter: FlutterFragment,
    viewPager: ViewPager2,
    appBarLayout: AppBarLayout,
    navigation: (String) -> Unit
) : FragmentStateAdapter(
    activity
) {

    private val mAppBarLayout: AppBarLayout
    private val mViewPager: ViewPager2
    private val mFlutter: FlutterFragment

    private var mNavigation: (String) -> Unit

    private lateinit var mLauncher: LauncherFragment
    private lateinit var mHome: HomeFragment
    private lateinit var mApps: AppsFragment
    private lateinit var mSettings: SettingsFragment
    private lateinit var mError: ErrorFragment

    init {
        // 初始化变量
        // 传入ViewPager2
        mViewPager = viewPager
        // 传入FlutterFragment
        mFlutter = flutter
        mAppBarLayout = appBarLayout
        // 传入导航函数
        mNavigation = navigation
    }

    /**
     * 返回页面数量
     */
    override fun getItemCount(): Int {
        return intArrayOf(
            launcher,
            home,
            apps,
            settings
        ).size
    }

    /**
     * 返回Fragment页面
     */
    override fun createFragment(position: Int): Fragment {
        initFragment()
        return when (position) {
            launcher -> mLauncher
            home -> mHome
            apps -> mApps
            settings -> mSettings
            else -> mError
        }
    }

    /**
     * 加载Fragment
     */
    private fun initFragment() {

        mLauncher = LauncherFragment.newInstance(
            appBarLayout = mAppBarLayout
        )
        mHome = HomeFragment.newInstance(
            flutterFragment = mFlutter
        )



        // 应用
        mApps = AppsFragment.newInstance(
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
            appBarLayout: AppBarLayout,
            navigation: (String) -> Unit
        ): ContentAdapter {
            return ContentAdapter(
                activity = activity,
                flutter = flutter,
                viewPager = viewPager,
                appBarLayout = appBarLayout,
                navigation = navigation
            )
        }

        // 页面代码，从0开始，以此类推
        const val launcher: Int = 0
        const val home: Int = 1
        const val apps: Int = 2
        const val settings: Int = 3
    }
}