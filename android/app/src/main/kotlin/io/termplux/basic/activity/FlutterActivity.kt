package io.termplux.basic.activity

import android.os.Bundle
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import io.flutter.embedding.engine.FlutterEngine

class FlutterActivity : FlutterBoostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
    }

    companion object {

    }
}