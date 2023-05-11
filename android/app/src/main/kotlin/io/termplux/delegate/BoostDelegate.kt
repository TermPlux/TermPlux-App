package io.termplux.delegate

import android.content.Intent
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.termplux.activity.TermPluxActivity
import io.termplux.activity.TermPluxFlutter


class BoostDelegate : FlutterBoostDelegate {

    init {

    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {
        // 这里根据options.pageName来判断你想跳转哪个页面，这里简单给一个
        // 这里根据options.pageName来判断你想跳转哪个页面，这里简单给一个
        val intent = Intent(
            FlutterBoost.instance().currentActivity(),
            TermPluxActivity().javaClass
        )
        FlutterBoost.instance().currentActivity().startActivityForResult(
            intent, options.requestCode()
        )
    }

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