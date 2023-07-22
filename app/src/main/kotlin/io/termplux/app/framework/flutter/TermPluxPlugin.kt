package io.termplux.app.framework.flutter

import android.app.Application
import android.util.Log
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.app.fragment.ReturnFragment
import io.termplux.app.framework.base.BaseClass
import java.lang.ref.WeakReference

open class TermPluxPlugin : io.termplux.app.framework.base.BaseClass() {

    override fun initFlutterBoost(application: Application) {
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                this@TermPluxPlugin,
                this@TermPluxPlugin
            )
        }
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun onFlutterCreated(flutterView: FlutterView?) {

    }

    override fun onFlutterDestroy(flutterView: FlutterView?) {

    }

    /**
     * 插件注册
     */
    override fun onStart(engine: FlutterEngine?) {
        engine?.let {
            try {
                it.plugins.add(this@TermPluxPlugin).run {
                    GeneratedPluginRegistrant.registerWith(it)
                }
            } catch (e: Exception) {
                Log.e(io.termplux.app.framework.flutter.TermPluxPlugin.Companion.tag, Log.getStackTraceString(e))
            }
        }
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {

    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {

    }

    companion object {
        const val tag: String = "EcosedPlugin"
    }
}