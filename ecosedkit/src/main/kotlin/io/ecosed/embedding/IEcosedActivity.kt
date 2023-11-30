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
package io.ecosed.embedding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
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
     * 将EcosedDroid附加到Activity.
     * @param activity 要附加的Activity.
     */
    fun IEcosedActivity.attachEcosed(
        activity: FragmentActivity,
        lifecycle: Lifecycle,
    )

    /**
     * 设置内容空间,it为仪表盘.
     */
    fun IEcosedActivity.setContentSpace(block: (flutter: View) -> Unit)

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
        bundle: Bundle? = null,
    ): T?

    /**
     * 将EcosedDroid与Activity分离.
     */
    fun IEcosedActivity.detachEcosed()
}