package io.termplux.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import io.termplux.R

class SettingsFragment(settings: () -> Unit) : PreferenceFragmentCompat() {

    private var mSettings: () -> Unit

    init {
        mSettings = settings
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val mode: SwitchPreference? = findPreference("running_mode")
        val settings: Preference? = findPreference("navigation_settings")

        settings?.setOnPreferenceClickListener {
            if (mode?.isChecked == true) mSettings()
            true
        }
    }

    companion object {
        fun newInstance(settings: () -> Unit): SettingsFragment{
            return SettingsFragment(settings = settings)
        }
    }
}