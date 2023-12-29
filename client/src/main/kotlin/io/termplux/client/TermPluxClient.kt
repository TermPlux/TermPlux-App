/**
 * Copyright EcosedKit
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
package io.termplux.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import io.termplux.aidl.TermPlux
import io.termplux.plugin.EcosedMethodCall
import io.termplux.plugin.EcosedResult
import io.termplux.plugin.EcosedPlugin
import io.termplux.plugin.PluginBinding

class TermPluxClient private constructor() : EcosedPlugin(), ServiceConnection, TermPluxCallBack {

    override val channel: String
        get() = mChannelName

    private lateinit var mIntent: Intent

    /** 服务AIDL接口 */
    private var mFramework: TermPlux? = null

    /** 服务绑定状态 */
    private var mIsBind: Boolean = false

    override fun onEcosedAdded(binding: PluginBinding) {
        super.onEcosedAdded(binding)
        mIntent = Intent(this@TermPluxClient, TermPluxService().javaClass)
        mIntent.action = action

        startService(mIntent)
        bindEcosed()

    }

    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
        super.onEcosedMethodCall(call, result)
        when (call.method) {
            mMethodDebug -> result.success(isDebug)
            mMethodIsBinding -> result.success(mIsBind)
            mMethodStartService -> startService(mIntent)
            mMethodBindService -> bindEcosed()
            mMethodUnbindService -> unbindEcosed()

            "" -> result.success({

            })

            mMethodShizukuVersion -> result.success(getShizukuVersion())
            else -> result.notImplemented()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mFramework = TermPlux.Stub.asInterface(service)
        when {
            mFramework != null -> {
                mIsBind = true
                callBack {
                    onEcosedConnected()
                }
            }

            else -> if (isDebug) Log.e(
                tag, "AIDL接口获取失败 - onServiceConnected"
            )
        }
        when {
            isDebug -> Log.i(
                tag, "服务已连接 - onServiceConnected"
            )
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
        Toast.makeText(this@TermPluxClient, "onEcosedConnected", Toast.LENGTH_SHORT).show()
    }

    override fun onEcosedDisconnected() {
        Toast.makeText(this@TermPluxClient, "onEcosedDisconnected", Toast.LENGTH_SHORT).show()
    }

    override fun onEcosedDead() {
        Toast.makeText(this@TermPluxClient, "onEcosedDead", Toast.LENGTH_SHORT).show()
    }

    override fun onEcosedUnbind() {
        Toast.makeText(this@TermPluxClient, "onEcosedUnbind", Toast.LENGTH_SHORT).show()
    }

    /**
     * 绑定服务
     */
    private fun bindEcosed() {
        try {
            if (!mIsBind) {
                bindService(
                    mIntent,
                    this@TermPluxClient,
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
    private fun unbindEcosed() {
        try {
            if (mIsBind) {
                unbindService(
                    this@TermPluxClient
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

    private fun getShizukuVersion(): String? {
        return try {
            if (mIsBind) {
                if (mFramework != null) {
                    mFramework!!.shizukuVersion
                } else {
                    callBack {
                        onEcosedUnbind()
                    }
                    null
                }
            } else {
                callBack {
                    onEcosedUnbind()
                }
                null
            }
        } catch (e: Exception) {
            if (isDebug) {
                Log.e(tag, "getShizukuVersion", e)
            }
            null
        }
    }

    private fun callBack(
        function: TermPluxCallBack.() -> Unit,
    ) {
        this@TermPluxClient.function()
    }

    companion object {
        internal const val mChannelName: String = "ecosed_droid"
        internal const val mMethodDebug: String = "is_binding"
        internal const val mMethodIsBinding: String = "is_debug"
        internal const val mMethodStartService: String = "start_service"
        internal const val mMethodBindService: String = "bind_service"
        internal const val mMethodUnbindService: String = "unbind_service"

        internal const val mMethodShizukuVersion: String = "shizuku_version"

        internal const val tag: String = "EcosedClient"
        internal const val action: String = "io.ecosed.droid.action"
        fun build(): TermPluxClient = TermPluxClient()
    }
}