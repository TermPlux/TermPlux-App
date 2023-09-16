package io.ecosed.libecosed_example

import android.app.Application
import io.ecosed.droid.app.EcosedAppImpl
import io.ecosed.droid.app.EcosedAppUtils
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.EcosedEngine

class MyApplication : Application(), EcosedAppImpl by EcosedAppUtils<MyApplication>() {

    override fun onCreate() {
        super.onCreate()
        attachUtils(application = this@MyApplication)
    }

    override fun init() {
        log("init")
    }

    override fun initSDKs() {
        log("initSDKs")
    }

    override fun initSDKInitialized() {
        log("initSDKInitialized")
    }

    override fun getPluginEngine(): EcosedEngine {
        return engine
    }

    override fun getEcosedClient(): EcosedClient {
        return MyClient()
    }
}