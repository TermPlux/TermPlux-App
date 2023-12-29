package io.termplux.hybrid

import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions

internal class FlutterDelegate private constructor() : FlutterBoostDelegate {

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {

    }

    companion object {
        fun build(): FlutterDelegate {
            return FlutterDelegate()
        }
    }
}