package io.ecosed.droid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.ecosed.droid.fragment.AppsFragment
import io.ecosed.droid.fragment.EmptyFragment
import io.ecosed.droid.fragment.MainFragment
import io.ecosed.droid.fragment.SettingsFragment

internal class PagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return arrayListOf(main, apps, settings).size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            main -> MainFragment.newInstance()
            apps -> AppsFragment.newInstance()
            settings -> SettingsFragment.newInstance()
            else -> EmptyFragment.newInstance()
        }
    }

    companion object {
        const val main: Int = 0
        const val apps: Int = 1
        const val settings: Int = 2
    }
}