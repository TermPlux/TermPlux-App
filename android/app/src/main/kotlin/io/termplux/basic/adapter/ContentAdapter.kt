package io.termplux.basic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import io.flutter.embedding.android.FlutterFragment
import io.termplux.basic.fragment.*

class ContentAdapter constructor(
    activity: FragmentActivity,
    flutter: FlutterFragment,
    appBarLayout: AppBarLayout,
    current: (Int) -> Unit,
    navigation: (String) -> Unit
) : FragmentStateAdapter(
    activity
) {

    private val mAppBarLayout: AppBarLayout
    private val mCurrent: (Int) -> Unit
    private val mFlutter: FlutterFragment

    private var mNavigation: (String) -> Unit

    private lateinit var mLauncher: LauncherFragment
    private lateinit var mApps: AppsFragment
    private lateinit var mSettings: SettingsFragment
    private lateinit var mError: ErrorFragment

    init {
        // 初始化变量
        mCurrent = current
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
            home -> mFlutter
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
        // 应用
        mApps = AppsFragment.newInstance(
            current = mCurrent
        )

        // 设置
        mSettings = SettingsFragment.newInstance(
            navigation = mNavigation
        )

        // 错误提示
        mError = ErrorFragment.newInstance(
            current = mCurrent
        )
    }

    companion object {

        fun newInstance(
            activity: FragmentActivity,
            flutter: FlutterFragment,
            appBarLayout: AppBarLayout,
            current: (Int) -> Unit,
            navigation: (String) -> Unit
        ): ContentAdapter {
            return ContentAdapter(
                activity = activity,
                flutter = flutter,
                appBarLayout = appBarLayout,
                current = current,
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