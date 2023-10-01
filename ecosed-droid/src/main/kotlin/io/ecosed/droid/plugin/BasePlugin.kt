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
package io.ecosed.droid.plugin

import android.content.Context
import android.content.ContextWrapper
import io.ecosed.droid.app.EcosedMethodCall
import io.ecosed.droid.app.EcosedResult

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/EcosedDroid
 * 时间: 2023/10/01
 * 描述: 基本插件
 * 文档: https://github.com/ecosed/EcosedDroid/blob/master/README.md
 */
abstract class BasePlugin : ContextWrapper(null) {

    /** 插件通道 */
    private lateinit var mPluginChannel: PluginChannel

    /** 是否调试模式 */
    private var mDebug: Boolean = false

    /**
     * 附加基本上下文
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    /**
     * 插件添加时执行
     */
    internal fun onEcosedAdded(binding: PluginBinding) {
        // 初始化插件通道
        mPluginChannel = PluginChannel(
            binding = binding, channel = channel
        )
        // 插件附加基本上下文
        attachBaseContext(
            base = mPluginChannel.getContext()
        )
        // 获取是否调试模式
        mDebug = mPluginChannel.isDebug()
        // 设置调用
        mPluginChannel.setMethodCallHandler(
            handler = this@BasePlugin
        )
    }

    /** 获取插件通道 */
    internal val getPluginChannel: PluginChannel
        get() = mPluginChannel

    /** 需要子类重写的通道名称 */
    abstract val channel: String

    /** 供子类使用的判断调试模式的接口 */
    protected val isDebug: Boolean = mDebug

    /**
     * 插件调用方法
     */
    open fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult) = Unit
}