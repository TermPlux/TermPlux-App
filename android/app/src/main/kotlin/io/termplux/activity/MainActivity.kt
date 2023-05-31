package io.termplux.activity

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.EdgeToEdgeUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.interfaces.LifeCircleListener
import com.kongzue.baseframework.util.JumpParameter
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.termplux.R
import io.termplux.fragment.MainFragment

class MainActivity : BaseActivity() {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

    private var mainFragment: MainFragment? = null

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mToolbar: MaterialToolbar

    private lateinit var newMainFragment: MainFragment

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
                FragmentContainerView(mContext).apply {
                    id = R.id.flutter_container
                    background = ContextCompat.getDrawable(
                        mContext,
                        R.drawable.custom_wallpaper_24
                    )
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
        // 片段管理器
        mFragmentManager = supportFragmentManager
        mainFragment = mFragmentManager.findFragmentByTag(
            tagFlutterBoostFragment
        ) as MainFragment?
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setDarkStatusBarTheme(true)
        setDarkNavigationBarTheme(true)
        setNavigationBarBackgroundColor(Color.TRANSPARENT)
        // 操作栏
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setIcon(R.drawable.baseline_terminal_24)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initDatas(parameter: JumpParameter?) {
        // 初始化FlutterBoostFragment
        newMainFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
            MainFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(true)
            .build()
        // 显示Fragment
        if (mainFragment == null) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    mainFragment = newMainFragment
                    add(
                        R.id.flutter_container,
                        newMainFragment,
                        tagFlutterBoostFragment
                    )
                }
            )
        }
    }

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            object : LifeCircleListener() {
                override fun onDestroy() {
                    super.onDestroy()
                    mainFragment = null
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
            android.R.id.home -> onBack()
            R.id.action_settings -> {
                //current(item = ContentAdapter.settings)
            }
        }
        return true
    }

    override fun onPostResume() {
        super.onPostResume()
        newMainFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        newMainFragment.onNewIntent(intent)
    }

    override fun onBack(): Boolean {
        super.onBack()
        newMainFragment.onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        newMainFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        newMainFragment.onUserLeaveHint()
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        newMainFragment.onTrimMemory(level)
    }

    companion object {
        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"
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