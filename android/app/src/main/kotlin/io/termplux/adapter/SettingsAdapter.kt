package io.termplux.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.termplux.fragment.SettingsFragment

class SettingsAdapter constructor(
    fragment: Fragment,
    settings: () -> Unit
) : FragmentStateAdapter(
    fragment
) {

    private val mNavigationToSettings: () -> Unit
    private val mSettingsFragment: SettingsFragment

    init {
        mNavigationToSettings = settings
        mSettingsFragment = SettingsFragment.newInstance(settings = settings)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return mSettingsFragment
    }
}