package io.ecosed.hybrid

import android.app.Application
import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.engine.EcosedEngine
import io.ecosed.plugin.EcosedPlugin

class HybridFlutter: HybridWrapper {

    private val engine: EcosedEngine = object : EcosedEngine() {

    }

    private val plugin: EcosedPlugin = object : EcosedPlugin() {

        override val channel: String
            get() = ""

    }

    override fun init(application: Application) {
        FlutterBoost.instance().setup(
            application,
            FlutterDelegate.build(),
            FlutterCallBack.build(plugin = engine)
        )
    }
}