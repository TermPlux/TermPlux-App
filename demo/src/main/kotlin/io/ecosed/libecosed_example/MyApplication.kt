package io.ecosed.libecosed_example

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.ecosed.libecosed.LibEcosedBuilder
import io.ecosed.libecosed.LibEcosedImpl
import io.ecosed.plugin.EcosedApplication
import io.ecosed.plugin.EcosedExtension
import io.ecosed.plugin.EcosedHost
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.LibEcosed
import io.ecosed.plugin.PluginEngine
import io.ecosed.plugin.PluginExecutor

class MyApplication : Application(), EcosedApplication, LibEcosedImpl by LibEcosedBuilder {

    private lateinit var mEngine: PluginEngine

    private val host: EcosedHost = object : EcosedHost {

        override val getPluginEngine: PluginEngine
            get() = mEngine

        override val getExtension: EcosedExtension?
            get() = super.getExtension

        override val getLibEcosed: LibEcosed
            get() = mLibEcosed

        override val getPluginList: ArrayList<EcosedPlugin>
            get() = arrayListOf()

        override val getMainFragment: Fragment
            get() = MainFragment()

        override val getProductLogo: Drawable?
            get() = ContextCompat.getDrawable(
                this@MyApplication,
                R.drawable.baseline_keyboard_command_key_24
            )

        override val isDebug: Boolean
            get() = BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        mEngine = PluginEngine.build(
            application = this@MyApplication,
            isUseHiddenApi = true
        )
        mEngine.attach()

    }

    override fun onTerminate() {
        super.onTerminate()
        mEngine.detach()
    }

    override val getEngineHost: EcosedHost
        get() = host
}