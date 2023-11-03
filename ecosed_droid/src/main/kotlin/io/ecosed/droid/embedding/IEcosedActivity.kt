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
package io.ecosed.droid.embedding

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Lifecycle

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/EcosedDroid
 * 时间: 2023/10/01
 * 描述: EcosedActivity接口
 * 文档: https://github.com/ecosed/EcosedDroid/blob/master/README.md
 */
interface IEcosedActivity {

    /**
     * 将EcosedDroid附加到Activity
     * @param activity 要附加的Activity
     */
    fun IEcosedActivity.attachEcosed(
        activity: Activity,
        lifecycle: Lifecycle
    )

    /**
     * 将EcosedDroid与Activity分离
     */
    fun IEcosedActivity.detachEcosed()

//    /**
//     * 设置布局,EcosedLauncher标记为true则设置主页布局,false为整个Activity的布局.
//     * @param content 页面布局
//     */
//    fun IEcosedActivity.setContentComposable(content: @Composable () -> Unit)

    /**
     * 调用插件代码的方法.
     * @param channel 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值.
     */
    fun <T> IEcosedActivity.execMethodCall(
        channel: String,
        method: String,
        bundle: Bundle? = null
    ): T?

    fun IEcosedActivity.toast(obj: Any)
    fun IEcosedActivity.log(obj: Any)
    fun IEcosedActivity.openUrl(url: String)
    fun IEcosedActivity.openApp(packageName: String)
    fun IEcosedActivity.isInstallApp(packageName: String): Boolean


}