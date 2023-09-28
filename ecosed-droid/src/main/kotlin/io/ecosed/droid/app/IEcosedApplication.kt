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
import android.content.ContextWrapper
import android.os.Bundle

interface IEcosedApplication {

    /** 内部接口 */
    val engine: Any

    /** 内部接口 */
    val host: Any

    /**
     *
     */
    fun IEcosedApplication.attachEcosed(
        application: Application,
        host: Any,
    )

    /**
     * 调用插件代码的方法.
     * @param name 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值.
     */
    fun <T> IEcosedApplication.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?,
    ): T?

    fun IEcosedApplication.toast(obj: Any)
    fun IEcosedApplication.log(obj: Any)
}