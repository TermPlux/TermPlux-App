package io.termplux.hybrid

import android.app.Application

interface HybridWrapper {

    fun withApplication(application: Application): HybridWrapper
    fun build(): HybridFlutter

}