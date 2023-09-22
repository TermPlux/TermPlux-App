package io.ecosed.droid.app

import android.app.Application
import android.content.ContextWrapper
import io.ecosed.droid.plugin.EcosedClient

interface IEcosedApplication {

    fun IEcosedApplication.attachEcosed(
        application: Application,
        host: EcosedHost
    )

//    fun init()
//    fun initSDKs()
//    fun initSDKInitialized()

//    /** 获取应用程序主机. */
//    fun getEcosedClient(): EcosedClient

    val engine: ContextWrapper
    val host: EcosedHost

    fun IEcosedApplication.toast(obj: Any)
    fun IEcosedApplication.log(obj: Any)
}