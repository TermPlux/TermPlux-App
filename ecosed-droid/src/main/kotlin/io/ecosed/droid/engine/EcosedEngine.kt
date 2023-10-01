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
package io.ecosed.droid.engine

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import io.ecosed.droid.app.EcosedHost
import io.ecosed.droid.app.IEcosedApplication
import io.ecosed.droid.client.EcosedClient
import io.ecosed.droid.plugin.BasePlugin
import io.ecosed.droid.plugin.PluginBinding

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件引擎
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
internal class EcosedEngine private constructor() : ContextWrapper(null) {

    /** 应用程序全局类. */
    private lateinit var mApp: Application

    /** 基础上下文. */
    private lateinit var mBase: Context

    /** 客户端组件. */
    private lateinit var mHost: EcosedHost

    /** 应用程序全局上下文, 非UI上下文. */
    private lateinit var mContext: Context

    /**  */
    private lateinit var mClient: EcosedClient

    /** 插件绑定器. */
    private var mBinding: PluginBinding? = null

    /** 插件列表. */
    private var mPluginList: ArrayList<BasePlugin>? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    /**
     * 将引擎附加到应用.
     */
    private fun attach() {
        when {
            (mPluginList == null) or (mBinding == null) -> apply {
                // 引擎附加基本上下文
                attachBaseContext(
                    base = mBase
                )
                // 初始化客户端组件
                mClient = EcosedClient.build()
                // 初始化插件绑定器.
                mBinding = PluginBinding(
                    context = mContext, debug = mHost.isDebug()
                )
                // 初始化插件列表.
                mPluginList = arrayListOf()
                // 加载框架
                mClient.let { ecosed ->
                    mBinding?.let { binding ->
                        ecosed.apply {
                            try {
                                onEcosedAdded(binding = binding)
                                if (mHost.isDebug()) {
                                    Log.d(tag, "框架已加载")
                                }
                            } catch (e: Exception) {
                                if (mHost.isDebug()) {
                                    Log.e(tag, "框架加载失败!", e)
                                }
                            }
                        }
                    }.run {
                        mPluginList?.add(element = ecosed)
                        if (mHost.isDebug()) {
                            Log.d(tag, "框架已添加到插件列表")
                        }
                    }
                }
                // 添加所有插件.
                mHost.getPluginList()?.let { plugins ->
                    mBinding?.let { binding ->
                        plugins.forEach { plugin ->
                            plugin.apply {
                                try {
                                    onEcosedAdded(binding = binding)
                                    if (mHost.isDebug()) {
                                        Log.d(tag, "插件${plugin.javaClass.name}已加载")
                                    }
                                } catch (e: Exception) {
                                    if (mHost.isDebug()) {
                                        Log.e(tag, "插件添加失败!", e)
                                    }
                                }
                            }
                        }
                    }.run {
                        plugins.forEach { plugin ->
                            mPluginList?.add(element = plugin)
                            if (mHost.isDebug()) {
                                Log.d(tag, "插件${plugin.javaClass.name}已添加到插件列表")
                            }
                        }
                    }
                }
            }

            else -> if (mHost.isDebug()) {
                Log.e(tag, "请勿重复执行attach!")
            }
        }
    }

    /**
     * 调用插件代码的方法.
     * @param channel 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值,类型为Any?.
     */
    internal fun <T> execMethodCall(
        channel: String,
        method: String,
        bundle: Bundle?,
    ): T? {
        var result: T? = null
        try {
            mPluginList?.forEach { plugin ->
                plugin.getPluginChannel.let { pluginChannel ->
                    when (pluginChannel.getChannel()) {
                        channel -> {
                            result = pluginChannel.execMethodCall<T>(
                                name = channel, method = method, bundle = bundle
                            )
                            if (mHost.isDebug()) {
                                Log.d(
                                    tag,
                                    "插件代码调用成功!\n通道名称:${channel}.\n方法名称:${method}.\n返回结果:${result}."
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (mHost.isDebug()) {
                Log.e(tag, "插件代码调用失败!", e)
            }
        }
        return result
    }

    /**
     * 用于构建引擎的接口.
     */
    internal interface Builder {

        /**
         * 引擎构建函数.
         * @param application 传入Application.
         * @return 返回已构建的引擎.
         */
        fun create(application: Application): EcosedEngine
    }

    internal companion object : Builder {

        /** 用于打印日志的标签. */
        private const val tag: String = "PluginEngine"

        /**
         * 引擎构建函数.
         * @param application 传入Application.
         * @return 返回已构建的引擎.
         */
        override fun create(
            application: Application,
        ): EcosedEngine = EcosedEngine().let { engine ->
            return@let engine.apply {
                if (application is IEcosedApplication) {
                    application.apply {
                        mApp = application
                        mBase = baseContext
                        mContext = applicationContext
                        mHost = getHost as EcosedHost
                    }.run {
                        attach()
                    }
                } else error(
                    message = "错误:EcosedApplication接口未实现.\n" + "提示1:可能未在应用的Application全局类实现EcosedApplication接口.\n" + "提示2:应用的Application全局类可能未在AndroidManifest.xml中注册."
                )
            }
        }
    }
}