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
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import io.ecosed.droid.engine.EcosedEngine
import org.lsposed.hiddenapibypass.HiddenApiBypass

class EcosedApp<YourApplication : IEcosedApp> : ContextWrapper(null),
    IEcosedApp, EcosedAppContent {

    private val mainHandler = Handler(Looper.getMainLooper())

    private lateinit var mApplication: Application

    private lateinit var mEngine: EcosedEngine

    private lateinit var mYourApplication: YourApplication

    private lateinit var mHost: EcosedAppHost

    private var mParent: () -> Unit = {}
    private var mBody: () -> Unit = {}
    private var mInit: () -> Unit = {}
    private var mInitSDKs: () -> Unit = {}
    private var mInitSDKInitialized: () -> Unit = {}

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

    override fun <T> IEcosedApp.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?,
    ): T? = engineUnit {
        return@engineUnit execMethodCall<T>(
            name = name,
            method = method,
            bundle = bundle
        )
    }





    @Suppress("UNCHECKED_CAST")
    override fun IEcosedApp.onAttachEcosed(
        application: Application,
        content: EcosedAppContent.() -> Unit
    ) {
        // 附加基本上下文
        attachBaseContext(base = application.baseContext)
        // 获取应用程序全局类
        mApplication = application
        // 获取mME
        mYourApplication = mApplication as YourApplication

        content.invoke(this@EcosedApp)

        mParent()

        // 初始化引擎
        mEngine = EcosedEngine.create(
            application = mApplication
        )

        mInit()
        object : Thread() {
            override fun run() {
                mYourApplication.apply {
                    mInitSDKs()
                    synchronized(mYourApplication) {
                        mainHandler.post {
                            mInitSDKInitialized()
                        }
                    }
                }
            }
        }.start()



        mBody()
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
            message = "给我在Application类里用奥你小子"
        )
    }


    fun runOnMain(runnable: Runnable) {
        mainHandler.post {
            runnable.run()
        }
    }



    override fun IEcosedApp.toast(obj: Any) {
        try {
            mainHandler.post {
                log(obj.toString())
                if (mToast == null) {
                    mToast = Toast.makeText(
                        this@EcosedApp,
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

    override fun IEcosedApp.log(obj: Any) {
        Log.i(tag, obj.toString())
    }

    override var initialize: EcosedAppInitialize?
        get() = null
        set(value) {
            mInit = {
                value?.init()
            }
            mInitSDKs = {
                value?.initSDKs()
            }
            mInitSDKInitialized = {
                value?.initSDKInitialized()
            }
        }

    override var host: EcosedAppHost?
        get() = null
        set(value) = defaultUnit {
            value?.let {
                mHost = it
            }
        }

    override var parent: (() -> Unit)?
        get() = null
        set(value) {
            value?.let {
                mParent = it
            }
        }


    override var body: (() -> Unit)?
        get() = null
        set(value) {
            value?.let {
                mBody = it
            }
        }

    companion object {
        const val tag: String = "tag"
        const val mNull: String = ""
    }
}