package io.termplux.app.flutter

import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.app.FlutterApplication

interface Controller {

    fun initFlutterBoost(application: FlutterApplication)
    fun initFlutterFragment()

    fun getFragment(): FlutterBoostFragment

}