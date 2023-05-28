package io.termplux.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.termplux.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mContext: Context
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val settings: Preference? = findPreference("navigation_settings")
        settings?.setOnPreferenceClickListener {
            true
        }
    }
}