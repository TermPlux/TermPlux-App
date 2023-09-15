package io.ecosed.libecosed_example

import android.app.Application
import io.ecosed.libecosed.EcosedDroidAppImpl
import io.ecosed.libecosed.EcosedDroidAppUtils
import io.ecosed.libecosed.plugin.EcosedClient
import io.ecosed.libecosed.plugin.PluginEngine

class MyApplication : Application(), EcosedDroidAppImpl by EcosedDroidAppUtils<MyApplication>() {

    override fun onCreate() {
        super.onCreate()
        attachUtils(application = this@MyApplication)
    }

    override fun getPluginEngine(): PluginEngine {
        return engine
    }

    override fun getEcosedClient(): EcosedClient {
        return MyClient()
    }
}