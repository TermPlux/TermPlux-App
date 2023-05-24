package io.termplux.delegate

import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.termplux.activity.TermPluxFlutter

class BoostDelegate constructor(
    native: (FlutterBoostRouteOptions) -> Unit
) : FlutterBoostDelegate {

    private val mNative: (FlutterBoostRouteOptions) -> Unit

    init {
        mNative = native
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) = mNative(options)

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions) {
        val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
            TermPluxFlutter().javaClass
        )
            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
            .destroyEngineWithActivity(false)
            .uniqueId(options.uniqueId())
            .url(options.pageName())
            .urlParams(options.arguments())
            .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }
}