/**
 * Copyright EcosedDroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ecosed.droid.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.R
import io.ecosed.droid.engine.EcosedEngine
import org.lsposed.hiddenapibypass.HiddenApiBypass

class EcosedApplication<YourApplication : IEcosedApplication> : ContextWrapper(null), IEcosedApplication {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: EcosedEngine

    private lateinit var mYourApplication: YourApplication

    private lateinit var mHost: EcosedHost


    private var mToast: Toast? = null




    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }


    override val getEngine: Any
        get() = mEngine

    override val getHost: Any
        get() = mHost

    override fun <T> IEcosedApplication.execMethodCall(
        channel: String,
        method: String,
        bundle: Bundle?,
    ): T? = engineUnit {
        return@engineUnit execMethodCall<T>(
            channel = channel,
            method = method,
            bundle = bundle
        )
    }





    @Suppress("UNCHECKED_CAST")
    override fun IEcosedApplication.attachEcosed(application: Application, host: EcosedHost) {
        // 附加基本上下文
        attachBaseContext(base = application.baseContext)
        // 获取应用程序全局类
        mApplication = application
        // 获取mME
        mYourApplication = mApplication as YourApplication





        mHost = host

        // 初始化引擎
        mEngine = EcosedEngine.create(
            application = mApplication
        )

        // 创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                notificationChannel,
                application.getString(R.string.lib_name),
                importance
            ).apply {
                description = application.getString(R.string.lib_description)
            }
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        object : Thread() {
            override fun run() {
                mYourApplication.apply {

                    synchronized(mYourApplication) {
                        mainHandler.post {

                        }
                    }
                }
            }
        }.start()


    }

    private fun initialize(initialize: EcosedAppInitialize?) = defaultUnit<Unit> {
        initialize?.apply {
            init()
            object : Thread() {
                override fun run() {
                    mYourApplication.apply {
                        initSDKs()
                        synchronized(mYourApplication) {
                            mainHandler.post {
                                initSDKInitialized()
                            }
                        }
                    }
                }
            }.start()
        }
    }


    private fun <T> engineUnit(
        content: EcosedEngine.() -> T,
    ): T? = mEngine.content()

    private fun <T> defaultUnit(
        content: Application.() -> T,
    ): T = when (mYourApplication) {
        is Application -> {
            (mYourApplication as Application).content()
        }

        else -> error(
            message = "错误: EcosedApplication只能在Application中使用!"
        )
    }


    fun runOnMain(runnable: Runnable) {
        mainHandler.post {
            runnable.run()
        }
    }



    override fun IEcosedApplication.toast(obj: Any) {
        try {
            mainHandler.post {
                log(obj.toString())
                if (mToast == null) {
                    mToast = Toast.makeText(
                        this@EcosedApplication,
                        mNull,
                        Toast.LENGTH_SHORT
                    )
                }
                mToast?.setText(obj.toString())
                mToast?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun IEcosedApplication.log(obj: Any) {
        Log.i(tag, obj.toString())
    }

    companion object {
        const val notificationChannel: String = "id"
        const val tag: String = "tag"
        const val mNull: String = ""
    }
}