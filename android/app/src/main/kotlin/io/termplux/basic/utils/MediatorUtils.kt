package io.termplux.basic.utils

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediatorUtils constructor(
    bottomNavigation: BottomNavigationView,
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    home: () -> Unit,
    config: (
        tab: TabLayout.Tab,
        position: Int
    ) -> Unit

) {

    private val mBottomNavigationView: BottomNavigationView
    private val mTabLayout: TabLayout
    private val mViewPager2: ViewPager2
    private val mNavToHome: () -> Unit
    private val mConfig: (tab: TabLayout.Tab, position: Int) -> Unit

    private val map = mutableMapOf<MenuItem, Int>()

    init {
        mBottomNavigationView = bottomNavigation
        mTabLayout = tabLayout
        mViewPager2 = viewPager
        mConfig = config
        mNavToHome = home
        mBottomNavigationView.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    fun attach() {
        mBottomNavigationView.setOnItemSelectedListener { item ->
            mNavToHome().apply {
                if (mViewPager2.currentItem != map[item]) {
                    mViewPager2.currentItem = map[item]
                        ?: error("没有${item.title}对应的Fragment")
                }
            }
            true
        }.also {
            mViewPager2.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        mBottomNavigationView.selectedItemId =
                            mBottomNavigationView.menu[position].itemId
                    }
                }
            )
        }.also {
            TabLayoutMediator(
                mTabLayout, mViewPager2
            ) { tab: TabLayout.Tab, position: Int ->
                mConfig(tab, position)
            }.attach()
        }
    }
}