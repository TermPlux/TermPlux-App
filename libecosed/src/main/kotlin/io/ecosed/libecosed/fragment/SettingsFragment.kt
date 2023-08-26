package io.ecosed.libecosed.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import io.ecosed.libecosed.R
import io.ecosed.libecosed.settings.EcosedSettings

internal class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.setStorageDeviceProtected()
        preferenceManager.sharedPreferencesName = EcosedSettings.name
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
         //   background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_wallpaper_24)
        }
    }

    companion object {
        fun newInstance(): SettingsFragment{
            return SettingsFragment()
        }
    }
}