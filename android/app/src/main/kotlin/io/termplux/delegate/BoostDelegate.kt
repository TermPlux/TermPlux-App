package io.termplux.delegate

import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.termplux.activity.MainFlutter
import io.termplux.plugin.FlutterTermPluxPlugin
import java.lang.ref.WeakReference

class BoostDelegate constructor(
    plugin: FlutterTermPluxPlugin
) : FlutterBoostDelegate {

    private val mPlugin: FlutterTermPluxPlugin

    init {
        mPlugin = plugin
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {
        WeakReference(mPlugin).get()?.pushNativeRoute(options = options)
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {
        val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
            MainFlutter::class.java
        )
            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
            .destroyEngineWithActivity(false)
            .uniqueId(options?.uniqueId())
            .url(options?.pageName())
            .urlParams(options?.arguments())
            .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }
}