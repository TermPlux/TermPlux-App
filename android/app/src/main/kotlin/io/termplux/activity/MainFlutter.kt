package io.termplux.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.internal.EdgeToEdgeUtils
import com.idlefish.flutterboost.containers.FlutterBoostActivity

class MainFlutter : FlutterBoostActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
    }
}