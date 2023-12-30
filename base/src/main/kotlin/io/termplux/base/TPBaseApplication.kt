package io.termplux.base

import com.kongzue.baseframework.BaseApp
import io.termplux.hybrid.HybridFlutter

open class TPBaseApplication : BaseApp<TPBaseApplication>(), TPBaseApplicationWrapper {

    private lateinit var mHybridFlutter: HybridFlutter

    override fun init() {
        val hybrid = HybridFlutter.build()
        mHybridFlutter = hybrid.withApplication(
            application = this@TPBaseApplication
        ).build()
    }

    override fun initSDKs() {
        super.initSDKs()
    }

    override fun initSDKInitialized() {
        super.initSDKInitialized()
    }

    override val hybrid: HybridFlutter
        get() = mHybridFlutter
}