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
package io.ecosed.droid.client

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.util.Log
import io.ecosed.droid.EcosedFramework
import io.ecosed.droid.app.EcosedMethodCall
import io.ecosed.droid.app.EcosedResult
import io.ecosed.droid.plugin.BasePlugin
import io.ecosed.droid.service.EcosedService

internal class EcosedClient private constructor() : BasePlugin(), ServiceConnection,
    EcosedCallBack {

    override val channel: String
        get() = mChannelName

    private lateinit var mIntent: Intent

    /** 服务AIDL接口 */
    private var mFramework: EcosedFramework? = null

    /** 服务绑定状态 */
    private var mIsBind: Boolean = false

    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
        super.onEcosedMethodCall(call, result)
        mIntent = Intent(
//            action,
//            Uri.parse(null),
            this@EcosedClient,
            EcosedService().javaClass
        )
        //service.action = serviceAction
        when (call.method) {
            mMethodDebug -> result.success(isDebug)
            mMethodIsBinding -> result.success(mIsBind
            )
            mMethodStartService -> startService(mIntent)
            mMethodBindService -> bindEcosed()
            mMethodUnbindService -> unbindEcosed()
            else -> result.notImplemented()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mFramework = EcosedFramework.Stub.asInterface(service)
        if (mFramework != null) {
            mIsBind = true
            callBack {
                onEcosedConnected()
            }
        } else {
            if (isDebug) {
                Log.e(tag, "AIDL接口获取失败 - onServiceConnected")
            }
        }
        if (isDebug) {
            Log.i(tag, "服务已连接 - onServiceConnected")
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mIsBind = false
        mFramework = null
        unbindService(this)
        callBack {
            onEcosedDisconnected()
        }
        if (isDebug) {
            Log.i(tag, "服务意外断开连接 - onServiceDisconnected")
        }
    }

    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
    }

    override fun onNullBinding(name: ComponentName?) {
        super.onNullBinding(name)
        if (isDebug) {
            Log.e(tag, "Binder为空 - onNullBinding")
        }
    }

    override fun onEcosedConnected() {

    }

    override fun onEcosedDisconnected() {

    }

    override fun onEcosedDead() {

    }

    override fun onEcosedUnbind() {

    }

    /**
     * 绑定服务
     */
    fun bindEcosed() {
        try {
            if (!mIsBind) {
                bindService(
                    mIntent,
                    this@EcosedClient,
                    Context.BIND_AUTO_CREATE
                ).let {
                    if (!it) callBack {
                        onEcosedDead()
                    }
                }
            }
        } catch (e: Exception) {
            if (isDebug) {
                Log.e(tag, "bindEcosed", e)
            }
        }
    }

    /**
     * 解绑服务
     */
    fun unbindEcosed() {
        try {
            if (mIsBind) {
                unbindService(
                    this@EcosedClient
                ).run {
                    mIsBind = false
                    mFramework = null
                    callBack {
                        onEcosedDisconnected()
                    }
                    if (isDebug) {
                        Log.i(tag, "服务已断开连接 - onServiceDisconnected")
                    }
                }
            }
        } catch (e: Exception) {
            if (isDebug) {
                Log.e(tag, "unbindEcosed", e)
            }
        }
    }

    private fun callBack(
        function: EcosedCallBack.() -> Unit
    ) {
        this@EcosedClient.function()
    }

    internal companion object {
        internal const val mChannelName: String = "ecosed_droid"
        internal const val mMethodDebug: String = "is_binding"
        internal const val mMethodIsBinding: String = "is_debug"
        internal const val mMethodStartService: String = "start_service"
        internal const val mMethodBindService: String = "bind_service"
        internal const val mMethodUnbindService: String = "unbind_service"

        internal const val tag: String = "EcosedClient"
        internal const val action: String = "io.ecosed.droid.action"
        internal fun build(): EcosedClient = EcosedClient()
    }
}