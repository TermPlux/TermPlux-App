package io.ecosed.libecosed_example

import android.app.Application
import io.ecosed.droid.EcosedDroidAppImpl
import io.ecosed.droid.EcosedDroidAppUtils
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

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