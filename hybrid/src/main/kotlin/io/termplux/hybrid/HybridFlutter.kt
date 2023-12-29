package io.termplux.hybrid

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.idlefish.flutterboost.FlutterBoost

class HybridFlutter : ContextWrapper(null), HybridWrapper {

    private lateinit var mApplication: Application

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun withApplication(application: Application): HybridWrapper {
        return this@HybridFlutter.apply {
            mApplication = application
        }
    }

    override fun build(): HybridFlutter {
        return this@HybridFlutter.apply {
            attachBaseContext(
                base = mApplication.baseContext
            )
            FlutterBoost.instance().setup(
                mApplication,
                FlutterDelegate.build(),
                FlutterCallBack.build(
                    plugin = EnginePlugin.build()
                )
            )
        }
    }

    companion object {

        fun build(): HybridFlutter {
            return HybridFlutter()
        }
    }
}