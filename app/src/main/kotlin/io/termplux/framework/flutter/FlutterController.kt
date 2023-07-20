package io.termplux.app.flutter

import android.util.Log
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.app.FlutterApplication
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.app.fragment.MainFragment
import java.lang.ref.WeakReference

class FlutterController : FlutterPlugin, Controller, FlutterBoostDelegate, FlutterBoost.Callback, MethodChannel.MethodCallHandler {

    private lateinit var mFlutterFragment: FlutterBoostFragment

    override fun initFlutterBoost(application: FlutterApplication) {
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                this@FlutterController,
                this@FlutterController
            )
        }
    }

    override fun initFlutterFragment() {
        mFlutterFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
            MainFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(false)
            .build<MainFragment>()
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onStart(engine: FlutterEngine?) {
        engine?.let {
            try {
                it.plugins.add(this@FlutterController).run {
                    GeneratedPluginRegistrant.registerWith(it)
                }
            } catch (e: Exception){
                Log.e("Plugin", Log.getStackTraceString(e))
            }
        }
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

    override fun getFragment(): FlutterBoostFragment {
        return mFlutterFragment
    }
}