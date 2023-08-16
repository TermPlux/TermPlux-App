package io.ecosed.libecosed_example

import android.app.Activity
import android.app.Application
import android.content.Context
import io.ecosed.libecosed.LibEcosedBuilder
import io.ecosed.libecosed.LibEcosedImpl
import io.ecosed.plugin.EcosedApplication
import io.ecosed.plugin.EcosedHost
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.PluginEngine
import io.ecosed.plugin.pluginArrayOf

class MyApplication : Application(), EcosedApplication, LibEcosedImpl by LibEcosedBuilder {

    private lateinit var mEngine: PluginEngine

    private val host: EcosedHost = object : EcosedHost {

        override val getPluginEngine: PluginEngine
            get() = mEngine

        override val getLibEcosed: EcosedPlugin
            get() = mLibEcosed

        override val getPluginList: ArrayList<EcosedPlugin>
            get() = pluginArrayOf()

        override val getLaunchActivity: Activity
            get() = MainActivity()

        override val isDebug: Boolean
            get() = BuildConfig.DEBUG

        override val getPackageName: String
            get() = BuildConfig.APPLICATION_ID
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mEngine = PluginEngine.build(
            baseContext = base,
            application = this@MyApplication,
            isUseHiddenApi = true
        )
    }

    override fun onCreate() {
        super.onCreate()
        mEngine.attach()
    }

    override fun onTerminate() {
        super.onTerminate()
        mEngine.detach()
    }

    override val getEngineHost: EcosedHost
        get() = host
}