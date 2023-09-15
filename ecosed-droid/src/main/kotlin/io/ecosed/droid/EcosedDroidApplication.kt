package io.ecosed.droid

import android.app.Application
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

interface EcosedDroidAppImpl : EcosedApplication {

    fun attachUtils(application: Application)

    val engine: PluginEngine



    fun toast(obj: Any)
    fun log(obj: Any)
}

open class EcosedDroidAppUtils<YourApp : Application> : ContextWrapper(null), EcosedDroidAppImpl {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: PluginEngine

    var mme: YourApp? = null


    override val engine: PluginEngine
        get() = mEngine


    override fun attachUtils(application: Application) {
        attachBaseContext(application.baseContext)
        mApplication = application
        @Suppress("UNCHECKED_CAST")
        mme = mApplication as YourApp
        mEngine = PluginEngine.create(application = mApplication)

//        object : Thread() {
//            override fun run() {
//                synchronized(mme!!) {
//                    initSDKs()
//                    if (onSDKInitializedCallBack != null) {
//                        mainHandler.post {
//                            initSDKInitialized()
//                        }
//                    }
//                }
//            }
//        }.start()

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