package io.termplux.app.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.termplux.app.R

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.setStorageDeviceProtected()
        //preferenceManager.sharedPreferencesName = ShizukuSettings.NAME
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {
        fun newInstance(): PreferenceFragment{
            return PreferenceFragment()
        }
    }
}