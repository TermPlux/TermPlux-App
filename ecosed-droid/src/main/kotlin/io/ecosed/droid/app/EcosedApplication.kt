package io.ecosed.droid.app

import android.app.Application
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.engine.EcosedEngine

class EcosedApplication<YourApp : Application> : ContextWrapper(null), IEcosedApplication {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: EcosedEngine

    private lateinit var mME: YourApp


    override val engine: ContextWrapper
        get() = mEngine


    override fun Application.attachUtils(application: Application, host: EcosedHost) {
        // 附加基本上下文
        attachBaseContext(application.baseContext)
        // 获取应用程序全局类
        mApplication = application
        // 获取mME
        @Suppress("UNCHECKED_CAST")
        mME = mApplication as YourApp
        // 初始化引擎
        mEngine = EcosedEngine.create(
            application = mME
        )


        if (mME is IEcosedApplication){
            (mME as IEcosedApplication).apply {
                this@EcosedApplication.init()
                this@apply.init()
            }
        }

        object : Thread() {
            override fun run() {
                if (mME is IEcosedApplication){
                    (mME as IEcosedApplication).apply {
                        synchronized(mME) {
                            this@EcosedApplication.initSDKs()
                            this@apply.initSDKs()
                            mainHandler.post {
                                this@EcosedApplication.initSDKInitialized()
                                this@apply.initSDKInitialized()
                            }
                        }
                    }
                }
            }
        }.start()
    }


    fun runOnMain(runnable: Runnable) {
        mainHandler.post { runnable.run() }
    }

    override fun init() {

    }

    override fun initSDKs() {

    }

    override fun initSDKInitialized() {

    }

    private var toast: Toast? = null

    override fun Application.toast(obj: Any) {
        try {
            mainHandler.post {
                log(obj.toString())
                if (toast == null) {
                    toast = Toast.makeText(
                        this@EcosedApplication,
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

//    override fun getPluginEngine(): EcosedEngine {
//        return mEngine
//    }

    override fun getEcosedClient(): EcosedClient {
        TODO("Not yet implemented")
    }

    companion object {
        const val tag: String = "tag"
        const val mNull: String = ""
    }
}