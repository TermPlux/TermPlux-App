package io.ecosed.libecosed.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

internal object EcosedSettings {

    private const val LANGUAGE = "language"
    private const val NIGHT_MODE = "night_mode"

    private lateinit var mSharedPreferences: SharedPreferences

    fun getPreferences(): SharedPreferences {
        return mSharedPreferences
    }

    fun initialize(context: Context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

}