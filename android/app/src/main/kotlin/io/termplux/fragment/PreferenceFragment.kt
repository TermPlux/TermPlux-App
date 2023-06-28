package io.termplux.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.termplux.R

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {

        fun newInstance(): PreferenceFragment {
            return PreferenceFragment()
        }
    }
}