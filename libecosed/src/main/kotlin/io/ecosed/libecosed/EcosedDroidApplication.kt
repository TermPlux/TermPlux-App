package io.ecosed.libecosed

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.ecosed.libecosed.plugin.EcosedApplication
import io.ecosed.libecosed.plugin.EcosedClient
import io.ecosed.libecosed.plugin.PluginEngine

interface EcosedDroidAppImpl : EcosedApplication {

    fun attachUtils(application: Application)

    val engine: PluginEngine



    fun toast(obj: Any)
    fun log(obj: Any)
}

open class EcosedDroidAppUtils<YourApp : Application> : ContextWrapper(null), EcosedDroidAppImpl, LifecycleOwner {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var mApplication: Application

    private lateinit var mEngine: PluginEngine

    var mme: YourApp? = null


    private fun onCreateUtils() {

//        object : Thread() {
//            override fun run() {
//                synchronized(me) {
//                    isInitializedSDKs = false
//                    initSDKs()
//                    if (onSDKInitializedCallBack != null) {
//                        mainHandler.post {
//                            initSDKInitialized()
//                            onSDKInitializedCallBack.onInitialized()
//                            isInitializedSDKs = true
//                        }
//                    }
//                }
//            }
//        }.start()



        lifecycleRegistry = LifecycleRegistry(provider = this@EcosedDroidAppUtils)

        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        mEngine = PluginEngine.create(application = mApplication)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val engine: PluginEngine
        get() = mEngine


    override fun attachUtils(application: Application) {
        attachBaseContext(application.baseContext)
        mApplication = application
        @Suppress("UNCHECKED_CAST")
        mme = mApplication as YourApp

        onCreateUtils()
    }

    private var toast: Toast? = null
    private val mNull = ""

    override fun toast(obj: Any) {
        try {
            mainHandler.post {
                log(obj.toString())
                if (toast == null) {
                    toast = Toast.makeText(
                        this@EcosedDroidAppUtils,
                        mNull,
                        Toast.LENGTH_SHORT
                    )
                }
                toast?.setText(obj.toString())
                toast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun log(obj: Any) {
        Log.i(tag, obj.toString())
    }

    override fun getPluginEngine(): PluginEngine {
        return mEngine
    }

    override fun getEcosedClient(): EcosedClient {
        TODO("Not yet implemented")
    }

    companion object {
        const val tag: String = "tag"
    }
}