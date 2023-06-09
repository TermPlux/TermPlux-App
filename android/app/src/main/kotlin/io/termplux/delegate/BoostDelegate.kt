package io.termplux.delegate

import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.termplux.activity.MainActivity
import io.termplux.activity.MainFlutter
import java.lang.ref.WeakReference

class BoostDelegate constructor(
    plugin: FlutterPlugin
) : FlutterBoostDelegate {

    private val mNative: (FlutterBoostRouteOptions) -> Unit

    init {
        when {
            (plugin is MainActivity) -> {
                mNative = {
                    WeakReference(plugin).get()?.apply {
                        pushNativeRoute(
                            options = it
                        )
                    }
                }
            }

            else -> notPlugin()
        }
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) = mNative(options)

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

    private fun notPlugin(): Nothing {
        error("这tm不是MainActivity")
    }
}