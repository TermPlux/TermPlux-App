package io.termplux.app

import android.app.Application
import io.ecosed.hybrid.HybridFlutter

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        HybridFlutter().init(this)
    }
}