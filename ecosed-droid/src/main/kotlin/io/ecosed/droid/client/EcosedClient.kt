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

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import io.ecosed.droid.app.EcosedMethodCall
import io.ecosed.droid.app.EcosedResult
import io.ecosed.droid.plugin.BasePlugin

internal class EcosedClient private constructor() : BasePlugin(), ServiceConnection,
    EcosedCallBack {

    override val channel: String
        get() = mChannelName

    override fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) {
        super.onEcosedMethodCall(call, result)
        when (call.method) {
            mMethodDebug -> result.success(isDebug)
            mMethodStartService -> {

            }
            else -> result.notImplemented()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
    }

    override fun onNullBinding(name: ComponentName?) {
        super.onNullBinding(name)
    }

    override fun onEcosedConnected() {

    }

    override fun onEcosedDisconnected() {

    }

    override fun onEcosedDead() {

    }

    override fun onEcosedUnbind() {

    }

    internal companion object {
        internal const val mChannelName: String = "ecosed_droid"
        internal const val mMethodDebug: String = "is_debug"
        internal const val mMethodStartService: String = "start_service"

        internal fun build(): EcosedClient = EcosedClient()
    }
}