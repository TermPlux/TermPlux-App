package io.termplux.base

import android.app.Application
import io.termplux.hybrid.HybridFlutter

open class TPBaseApplication : Application(), TPBaseApplicationWrapper {

    private lateinit var mHybridFlutter: HybridFlutter

    override fun onCreate() {
        super.onCreate()
        val hybrid = HybridFlutter.build()
        mHybridFlutter = hybrid.withApplication(
            application = this@TPBaseApplication
        ).build()
    }

    override val hybrid: HybridFlutter
        get() = mHybridFlutter
}