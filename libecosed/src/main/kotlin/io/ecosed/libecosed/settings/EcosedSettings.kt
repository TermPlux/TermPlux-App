package io.ecosed.libecosed.settings

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import io.ecosed.libecosed.utils.EmptySharedPreferencesImpl

internal object EcosedSettings {


    const val name: String = "ecosed_settings"
    const val settingsDesktop: String = "desktop"
    const val settingsDynamicColor: String = "dynamic_colors"
    const val settingsBlackNight: String = "black_night"

    private lateinit var mSharedPreferences: SharedPreferences


    private fun getSettingsStorageContext(context: Context): Context {
        var storageContext: Context = context.createDeviceProtectedStorageContext()
        storageContext = object : ContextWrapper(storageContext) {
            override fun getSharedPreferences(name: String, mode: Int): SharedPreferences {
                return try {
                    super.getSharedPreferences(name, mode)
                } catch (e: IllegalStateException) {
                    EmptySharedPreferencesImpl()
                }
            }
        }
        return storageContext
    }


    fun getPreferences(): SharedPreferences {
        return mSharedPreferences
    }


    fun initialize(context: Context) {
        mSharedPreferences = getSettingsStorageContext(
            context
        ).getSharedPreferences(
            name,
            Context.MODE_PRIVATE
        )
    }
}