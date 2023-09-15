package io.ecosed.droid.app

import android.app.Application
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

interface EcosedAppImpl {

    fun Application.attachUtils(application: Application)

    fun init()
    fun initSDKs()
    fun initSDKInitialized()

    /** 获取插件引擎. */
    fun getPluginEngine(): PluginEngine

    /** 获取应用程序主机. */
    fun getEcosedClient(): EcosedClient

    val engine: PluginEngine

    fun Application.toast(obj: Any)
    fun Application.log(obj: Any)
}