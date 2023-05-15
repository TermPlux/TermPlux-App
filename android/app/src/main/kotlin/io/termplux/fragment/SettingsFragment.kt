package io.termplux.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.termplux.R
import io.termplux.ui.navigation.Screen

class SettingsFragment constructor(
    navigation: (String) -> Unit
) : PreferenceFragmentCompat() {

    private var mNavigation: () -> Unit

    private lateinit var mContext: Context
    private lateinit var mSharedPreferences: SharedPreferences

    init {
        mNavigation = {
            navigation(
                Screen.Settings.route
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val settings: Preference? = findPreference("navigation_settings")
        settings?.setOnPreferenceClickListener {
            mNavigation()
            true
        }
    }

    companion object{
        fun newInstance(
            navigation: (String) -> Unit
        ): SettingsFragment {
            return SettingsFragment(
                navigation = navigation
            )
        }
    }
}