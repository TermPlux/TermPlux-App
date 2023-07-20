package io.termplux.framework.base

import android.app.Application
import android.os.Bundle
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostDelegate
import io.flutter.embedding.android.FlutterEngineConfigurator
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel
import rikka.material.app.MaterialActivity

abstract class BaseClass : MaterialActivity(), FlutterBoostDelegate, FlutterBoost.Callback,
    FlutterPlugin, MethodChannel.MethodCallHandler, FlutterEngineConfigurator {

    private lateinit var mApplication: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        mApplication = application
        initFlutterBoost(application = mApplication)
        initFlutterFragment()
        super.onCreate(savedInstanceState)
    }

    abstract fun initFlutterBoost(application: Application)
    abstract fun initFlutterFragment()
}