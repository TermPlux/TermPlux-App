package io.termplux.activity

import android.annotation.SuppressLint
import android.content.*
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme
import com.kongzue.baseframework.interfaces.FragmentLayout
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes
import com.kongzue.baseframework.interfaces.SwipeBack
import com.kongzue.baseframework.util.AppManager
import com.kongzue.baseframework.util.FragmentChangeUtil
import com.kongzue.baseframework.util.JumpParameter
import io.termplux.R
import io.termplux.custom.DisableSwipeViewPager
import io.termplux.fragment.ContainerFragment
import io.termplux.fragment.SettingsFragment

@SuppressLint("NonConstantResourceId")
@SwipeBack(false)
@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColorRes(R.color.white)
@FragmentLayout(R.id.fragment_container)
class MainActivity : BaseActivity() {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

    private lateinit var mToolbar: MaterialToolbar

//    private lateinit var mViewPager2: ViewPager2
//    private lateinit var mBottomNavigationView: BottomNavigationView
//    private lateinit var mTabLayout: TabLayout

    private val container: ContainerFragment = ContainerFragment.newInstance()
    private val settings: SettingsFragment = SettingsFragment.newInstance()

    override fun resetContentView(): View {
        super.resetContentView()
        return LinearLayoutCompat(mContext).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppBarLayout(
                    mContext
                ).apply {
                    fitsSystemWindows = true
                    addView(
                        MaterialToolbar(
                            mContext
                        ).also { toolbar ->
                            setSupportActionBar(toolbar)
                            mToolbar = toolbar
                        },
                        AppBarLayout.LayoutParams(
                            AppBarLayout.LayoutParams.MATCH_PARENT,
                            AppBarLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                }.also { appBar ->
                    appBar.isLiftOnScroll = true
                    appBar.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(
                        mContext
                    )
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                DisableSwipeViewPager(
                    mContext
                ).apply {
                    id = R.id.fragment_container
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    @SuppressLint("RestrictedApi")
    override fun initViews() {

        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)


        // 加载TabLayout
//        mTabLayout = TabLayout(
//            mContext
//        ).apply {
//            setBackgroundColor(android.graphics.Color.TRANSPARENT)
//            tabMode = TabLayout.MODE_SCROLLABLE
//        }


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setIcon(R.drawable.baseline_terminal_24)
        actionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun initFragment(fragmentChangeUtil: FragmentChangeUtil?) {
        super.initFragment(fragmentChangeUtil)
        // 添加Fragment
        fragmentChangeUtil?.addFragment(container, true)
        //  fragmentChangeUtil?.addFragment(settings, true)
        // 默认切换到主页
        changeFragment(0)
    }

    override fun initDatas(parameter: JumpParameter?) {

    }

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            object : LifeCircleListener() {

                override fun onCreate() {
                    super.onCreate()

                }

                override fun onResume() {
                    super.onResume()
                }

                override fun onPause() {
                    super.onPause()
                }

                override fun onDestroy() {
                    super.onDestroy()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> changeFragment(0, R.anim.fade, R.anim.hold)
            R.id.action_settings -> {
                //current(item = ContentAdapter.settings)
            }
        }
        return true
    }


//    private fun fullScreenWebView(url: String) {
//        FullScreenDialog.show(
//            object : OnBindView<FullScreenDialog>(mWebView) {
//                override fun onBind(dialog: FullScreenDialog?, v: View?) {
//                    mWebView.loadUrl(url)
//                }
//            }
//        )
//    }


//    private fun mediator(navigationType: NavigationType, home: () -> Unit) {
//        MediatorUtils(
//            bottomNavigation = mBottomNavigationView,
//            tabLayout = mTabLayout,
//            viewPager = mViewPager2,
//            home = {
//                home()
//            }
//        ) { page, tab, position ->
//            page.apply {
//                orientation = when (navigationType) {
//                    NavigationType.BottomNavigation -> ViewPager2.ORIENTATION_HORIZONTAL
//                    NavigationType.NavigationRail -> ViewPager2.ORIENTATION_VERTICAL
//                    NavigationType.PermanentNavigationDrawer -> ViewPager2.ORIENTATION_VERTICAL
//                }
//                isUserInputEnabled = navigationType != NavigationType.NavigationRail
//            }
//            tab.apply {
//                text = arrayOf(
//                    getString(R.string.menu_launcher),
//                    getString(R.string.menu_apps),
//                    getString(R.string.menu_settings)
//                )[position]
//            }
//        }.attach()
//    }
//
//
//    /**
//     * ViewPager2切换页面
//     */
//    private fun current(item: Int) {
//        if (mViewPager2.currentItem != item) {
//            mViewPager2.currentItem = item
//        }
//    }
}