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

import io.ecosed.droid.plugin.BasePlugin

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件接口
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
//interface EcosedPlugins {
//
//    /** 插件被添加时执行. */
//    fun onEcosedAdded(binding: PluginBinding)
//
//    /** 获取插件通信通道. */
//    val getPluginChannel: PluginChannel
//}

abstract class EcosedPlugin: BasePlugin()

//abstract class EcosedPlugin: ContextWrapper(null) {
//
//    private lateinit var mPluginChannel: PluginChannel
//
//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//    }
//
//    internal fun onEcosedAdded(binding: PluginBinding) {
//        mPluginChannel = PluginChannel(binding = binding, channel = channel)
//        attachBaseContext(base =  mPluginChannel.getContext())
//        mPluginChannel.setMethodCallHandler(handler = this@EcosedPlugin)
//    }
//
//    internal val getPluginChannel: PluginChannel
//        get() = mPluginChannel
//
//    abstract val channel: String
//
//    open fun onEcosedMethodCall(call: MethodCall, result: Result) {
//
//    }
//}