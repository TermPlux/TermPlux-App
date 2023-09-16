package io.ecosed.droid.app

import android.app.Application
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.EcosedEngine

interface EcosedAppImpl {

    fun Application.attachUtils(application: Application)

    fun init()
    fun initSDKs()
    fun initSDKInitialized()

    /** 获取插件引擎. */
    fun getPluginEngine(): EcosedEngine

    /** 获取应用程序主机. */
    fun getEcosedClient(): EcosedClient

    val engine: EcosedEngine

    fun Application.toast(obj: Any)
    fun Application.log(obj: Any)
}