package io.ecosed.droid

import android.app.Application
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

class EcosedAppUtils<YourApp : Application> : ContextWrapper(null), EcosedAppImpl {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: PluginEngine

    private lateinit var mME: YourApp


    override val engine: PluginEngine
        get() = mEngine


    override fun Application.attachUtils(application: Application) {
        // 附加基本上下文
        attachBaseContext(application.baseContext)
        // 获取应用程序全局类
        mApplication = application
        // 获取mME
        @Suppress("UNCHECKED_CAST")
        mME = mApplication as YourApp
        // 初始化引擎
        mEngine = PluginEngine.create(application = mME)

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

    override fun Application.toast(obj: Any) {
        try {
            mainHandler.post {
                log(obj.toString())
                if (toast == null) {
                    toast = Toast.makeText(
                        this@EcosedAppUtils,
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

    override fun Application.log(obj: Any) {
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
        const val mNull: String = ""
    }
}