package io.ecosed.droid.utils

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class MediatorUtils constructor(
    bottomNavigation: BottomNavigationView,
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    home: () -> Unit,
    config: (
        page: ViewPager2,
        tab: TabLayout.Tab,
        position: Int
    ) -> Unit
) {

    private val mBottomNavigationView: BottomNavigationView
    private val mTabLayout: TabLayout
    private val mViewPager: ViewPager2
    private val mNavToHome: () -> Unit
    private val mConfig: (page: ViewPager2, tab: TabLayout.Tab, position: Int) -> Unit

    private val map = mutableMapOf<MenuItem, Int>()

    init {
        mBottomNavigationView = bottomNavigation
        mTabLayout = tabLayout
        mViewPager = viewPager
        mConfig = config
        mNavToHome = home
        mBottomNavigationView.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    internal fun attach() {
        mBottomNavigationView.setOnItemSelectedListener { item ->
            mNavToHome().apply {
                if (mViewPager.currentItem != map[item]) {
                    mViewPager.currentItem = map[item]
                        ?: error("没有${item.title}对应的View")
                }
            }
            true
        }
        mViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mBottomNavigationView.selectedItemId =
                        mBottomNavigationView.menu[position].itemId
                }
            }
        )
        TabLayoutMediator(
            mTabLayout, mViewPager
        ) { tab: TabLayout.Tab, position: Int ->
            mConfig(mViewPager, tab, position)
        }.attach()
    }
}