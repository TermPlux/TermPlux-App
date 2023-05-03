package io.termplux.basic.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.termplux.R
import io.termplux.app.ui.navigation.Screen

class Settings constructor(
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
        fun newInstance(navigation: (String) -> Unit): Settings{
            return Settings(navigation = navigation)
        }
    }
}