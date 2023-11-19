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
package io.ecosed.client

internal interface EcosedCallBack {

    /** 在服务绑定成功时回调 */
    fun onEcosedConnected()

    /** 在服务解绑或意外断开链接时回调 */
    fun onEcosedDisconnected()

    /** 在服务端服务未启动时绑定服务时回调 */
    fun onEcosedDead()

    /** 在未绑定服务状态下调用API时回调 */
    fun onEcosedUnbind()
}