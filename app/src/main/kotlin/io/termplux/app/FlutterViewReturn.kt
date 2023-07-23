package io.termplux.app

import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.FlutterView

interface FlutterViewReturn {

    fun onFlutterCreated(flutterView: FlutterView?)
    fun onFlutterDestroy(flutterView: FlutterView?)

    fun getFlutterFragment(): FlutterBoostFragment

}