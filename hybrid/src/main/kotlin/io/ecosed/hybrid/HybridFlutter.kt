package io.ecosed.hybrid

import android.app.Application
import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.engine.EcosedEngine

class HybridFlutter: HybridWrapper {

    private val engine: EcosedEngine = object : EcosedEngine() {

    }

    override fun init(application: Application) {
        FlutterBoost.instance().setup(
            application,
            FlutterDelegate.build(),
            FlutterCallBack.build(plugin = engine)
        )
    }
}