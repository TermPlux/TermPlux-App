package io.ecosed.droid.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import io.ecosed.droid.R
import io.ecosed.droid.settings.EcosedSettings

internal class SettingsFragment private constructor() : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.setStorageDeviceProtected()
        preferenceManager.sharedPreferencesName = EcosedSettings.name
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }


    companion object {
        fun newInstance(): SettingsFragment{
            return SettingsFragment()
        }
    }
}