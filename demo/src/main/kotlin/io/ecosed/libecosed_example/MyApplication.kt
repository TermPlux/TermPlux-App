package io.ecosed.libecosed_example

import android.app.Application
import io.ecosed.plugin.EcosedApplication
import io.ecosed.plugin.EcosedClient
import io.ecosed.plugin.PluginEngine

class MyApplication : Application(), EcosedApplication {

    private lateinit var mEngine: PluginEngine

    override fun onCreate() {
        super.onCreate()
        mEngine = PluginEngine.create(
            application = this@MyApplication
        )
    }

    override fun getEcosedClient(): EcosedClient {
        return MyClient()
    }

    override fun getPluginEngine(): PluginEngine {
        return mEngine
    }
}