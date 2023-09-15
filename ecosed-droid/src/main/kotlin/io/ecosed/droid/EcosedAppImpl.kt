package io.ecosed.droid

import android.app.Application
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

interface EcosedAppImpl : EcosedApplication {

    fun Application.attachUtils(application: Application)

    val engine: PluginEngine

    fun Application.toast(obj: Any)
    fun Application.log(obj: Any)
}