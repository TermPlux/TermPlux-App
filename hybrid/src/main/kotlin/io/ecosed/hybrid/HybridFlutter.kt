package io.ecosed.hybrid

import android.app.Application
import com.idlefish.flutterboost.FlutterBoost

class HybridFlutter {

    fun init(application: Application) {
        FlutterBoost.instance().setup(
            application,
            FlutterDelegate.build(),
            FlutterCallBack.build()
        )
    }
}