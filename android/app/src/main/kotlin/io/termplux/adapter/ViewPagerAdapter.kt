package io.termplux.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.flutter.embedding.android.FlutterFragment
import io.termplux.fragment.AppFragment
import io.termplux.fragment.ErrorFragment
import io.termplux.fragment.HomeFragment

class ViewPagerAdapter(
    activity: FragmentActivity,
    flutter: FlutterFragment,
    viewPager: ViewPager2
) : FragmentStateAdapter(
    activity
) {

    private var mViewPager: ViewPager2

    private var mFlutter: FlutterFragment
    private var mHome: HomeFragment
    private var mApps: AppFragment
    private val mError: ErrorFragment

    init {
        mViewPager = viewPager

        mHome = HomeFragment.newInstance(
            viewPager = mViewPager
        )

        mFlutter = flutter

        mApps = AppFragment.newInstance(
            viewPager = mViewPager
        )

        mError = ErrorFragment.newInstance(
            viewPager = mViewPager
        )
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            home -> mHome
            flutter -> mFlutter
            apps -> mApps
            else -> mError
        }
    }

    companion object {
        const val home: Int = 0
        const val flutter: Int = 1
        const val apps: Int = 2
    }
}