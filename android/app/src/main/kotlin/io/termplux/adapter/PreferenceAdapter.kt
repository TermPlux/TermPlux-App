package io.termplux.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.termplux.fragment.PreferenceFragment

class PreferenceAdapter constructor(
    activity: FragmentActivity,
    settings: () -> Unit
) : FragmentStateAdapter(
    activity
) {

    private val mNavigationToSettings: () -> Unit
    private val mPreferenceFragment: PreferenceFragment

    init {
        mNavigationToSettings = settings
        mPreferenceFragment = PreferenceFragment.newInstance(settings = settings)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return mPreferenceFragment
    }
}