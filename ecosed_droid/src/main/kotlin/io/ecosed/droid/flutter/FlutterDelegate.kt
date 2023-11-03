package io.ecosed.droid.flutter

import android.content.Intent
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.ecosed.droid.activity.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs

internal class FlutterDelegate private constructor() : FlutterBoostDelegate {

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {
        when (options.pageName()) {

        }
        val intent = Intent(
            FlutterBoost.instance().currentActivity(),
            FlutterActivity().javaClass
        )
        FlutterBoost.instance().currentActivity()
            .startActivityForResult(intent, options.requestCode())
    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions) {
        val intent =
            FlutterBoostActivity.CachedEngineIntentBuilder(FlutterActivity().javaClass)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
                .destroyEngineWithActivity(false)
                .uniqueId(options.uniqueId())
                .url(options.pageName())
                .urlParams(options.arguments())
                .build(FlutterBoost.instance().currentActivity())
        FlutterBoost.instance().currentActivity().startActivity(intent)
    }

    internal companion object {
        internal fun build(): FlutterDelegate {
            return FlutterDelegate()
        }
    }
}