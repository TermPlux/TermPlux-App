package io.ecosed.droid.app

import android.app.Application
import android.content.ContextWrapper
import io.ecosed.droid.plugin.EcosedClient

interface IEcosedApplication {

    fun Application.attachUtils(
        application: Application,
        host: EcosedHost
    )

    fun init()
    fun initSDKs()
    fun initSDKInitialized()

//    /** 获取插件引擎. */
//    fun getPluginEngine(): EcosedEngine

    /** 获取应用程序主机. */
    fun getEcosedClient(): EcosedClient

    val engine: ContextWrapper

    fun Application.toast(obj: Any)
    fun Application.log(obj: Any)
}