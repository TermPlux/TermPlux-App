package io.termplux.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.termplux.fragment.PreferenceFragment

class PreferenceAdapter constructor(
    activity: FragmentActivity
) : FragmentStateAdapter(
    activity
) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return PreferenceFragment.newInstance()
    }
}