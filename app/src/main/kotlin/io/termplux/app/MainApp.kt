package io.termplux.app

import android.app.Application
import io.termplux.hybrid.HybridFlutter

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val hybrid = HybridFlutter.build()
        val hy = hybrid.withApplication(application = this@MainApp).build()



    }
}