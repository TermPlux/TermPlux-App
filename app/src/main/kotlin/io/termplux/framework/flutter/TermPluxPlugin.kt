package io.termplux.framework.flutter

import android.app.Application
import android.util.Log
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.app.fragment.MainFragment
import io.termplux.framework.base.BaseClass
import java.lang.ref.WeakReference

open class TermPluxPlugin : BaseClass() {

    protected lateinit var mFlutter: FlutterFragment

    override fun initFlutterBoost(application: Application) {
        WeakReference(application).get()?.apply {
            FlutterBoost.instance().setup(
                this@apply,
                this@TermPluxPlugin,
                this@TermPluxPlugin
            )
        }
    }

    override fun initFlutterFragment() {
        mFlutter = FlutterBoostFragment.CachedEngineFragmentBuilder(
            MainFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(false)
            .build<MainFragment>()
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions?) {

    }

    override fun pushFlutterRoute(options: FlutterBoostRouteOptions?) {

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
                Log.e(tag, Log.getStackTraceString(e))
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