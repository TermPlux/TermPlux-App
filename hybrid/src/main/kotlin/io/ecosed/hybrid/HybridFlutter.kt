package io.ecosed.hybrid

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.idlefish.flutterboost.FlutterBoost
import io.ecosed.engine.EcosedEngine
import io.ecosed.plugin.EcosedMethodCall
import io.ecosed.plugin.EcosedPlugin
import io.ecosed.plugin.EcosedResult
import io.ecosed.plugin.PluginBinding

class HybridFlutter : ContextWrapper(null), HybridWrapper {

    private lateinit var mApplication: Application

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    private val mEngine: EcosedEngine = object : EcosedEngine() {

        override val hybridPlugin: EcosedPlugin
            get() = mPlugin
    }

    private val mPlugin: EcosedPlugin = object : EcosedPlugin() {

        override val channel: String
            get() = channelName

        override fun onEcosedAdded(binding: PluginBinding) {
            super.onEcosedAdded(binding)
        }

        override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
            super.onEcosedMethodCall(call, result)
        }
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
                    plugin = mEngine
                )
            )
        }
    }

    companion object {
        const val channelName: String = "hybrid_flutter"

        fun build(): HybridFlutter {
            return HybridFlutter()
        }
    }
}