package io.termplux.app.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.termplux.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}