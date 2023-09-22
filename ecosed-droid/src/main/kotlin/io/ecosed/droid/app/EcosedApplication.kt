package io.ecosed.droid.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.engine.EcosedEngine

class EcosedApplication<YourApplication : IEcosedApplication> : ContextWrapper(null), IEcosedApplication {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: EcosedEngine

    private lateinit var mYourApplication: YourApplication

    private lateinit var mHost: EcosedHost


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }



    override val engine: ContextWrapper
        get() = mEngine

    override val host: EcosedHost
        get() = mHost


    @Suppress("UNCHECKED_CAST")
    override fun IEcosedApplication.attachEcosed(
        application: Application,
        host: EcosedHost
    ) {
        // 附加基本上下文
        attachBaseContext(application.baseContext)
        // 获取应用程序全局类
        mApplication = application
        // 获取mME
        mYourApplication = mApplication as YourApplication


        mHost = host
        // 初始化引擎
        mEngine = EcosedEngine.create(
            application = mApplication
        )



//        if (mYourApplication is IEcosedApplication){
//            (mYourApplication as IEcosedApplication).apply {
//                this@EcosedApplication.init()
//                this@apply.init()
//            }
//        }
//
//        object : Thread() {
//            override fun run() {
//                if (mYourApplication is IEcosedApplication){
//                    (mYourApplication as IEcosedApplication).apply {
//                        synchronized(mYourApplication) {
//                            this@EcosedApplication.initSDKs()
//                            this@apply.initSDKs()
//                            mainHandler.post {
//                                this@EcosedApplication.initSDKInitialized()
//                                this@apply.initSDKInitialized()
//                            }
//                        }
//                    }
//                }
//            }
//        }.start()
    }


    fun runOnMain(runnable: Runnable) {
        mainHandler.post {
            runnable.run()
        }
    }

    private var toast: Toast? = null

    override fun IEcosedApplication.toast(obj: Any) {
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

    override fun IEcosedApplication.log(obj: Any) {
        Log.i(tag, obj.toString())
    }

//    override fun getEcosedClient(): EcosedClient {
//        TODO("Not yet implemented")
//    }

    companion object {
        const val tag: String = "tag"
        const val mNull: String = ""
    }
}