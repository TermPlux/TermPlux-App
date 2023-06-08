package io.termplux.plugin

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class FlutterTermPluxPlugin : FlutterBoostFragment(), FlutterPlugin, MethodCallHandler, TermPlux,
    DefaultLifecycleObserver, Runnable {


    private lateinit var mChannel: MethodChannel


    init {

    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        mChannel = MethodChannel(binding.binaryMessenger, plugin_channel)
        mChannel.setMethodCallHandler(this@FlutterTermPluxPlugin)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        mChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
    }

    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine) {
        super.cleanUpFlutterEngine(flutterEngine)

    }

    override fun run() {

    }

    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super<FlutterBoostFragment>.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ComposeView = attach(
        view = super.onCreateView(
            inflater,
            container,
            savedInstanceState
        ),
        flutterFragment = this@FlutterTermPluxPlugin
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super<FlutterBoostFragment>.onDestroy()
    }

    override fun init(application: Application) {

    }

    override fun configure(flutterEngine: FlutterEngine) {

    }

    override fun attach(view: View?, flutterFragment: FlutterFragment): ComposeView {
        return ComposeView(context)

    }

    override fun clean(flutterEngine: FlutterEngine) {

    }

    private fun findFlutterView(view: View?): FlutterView? {
        when {
            (view is FlutterView) -> return view
            (view is ViewGroup) -> for (i in 0 until view.childCount) {
                findFlutterView(view.getChildAt(i))?.let {
                    return it
                }
            }
        }
        return null
    }

    companion object {
        const val plugin_channel: String = "flutter_termplux"
        const val termplux_channel: String = "termplux_channel"
    }
}