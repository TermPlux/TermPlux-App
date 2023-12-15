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
package io.ecosed.plugin

import android.content.Context
import android.os.Bundle

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件通信通道
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
class PluginChannel(binding: PluginBinding, channel: String) {

    /** 插件绑定器. */
    private var mBinding: PluginBinding = binding

    /** 插件通道. */
    private var mChannel: String = channel

    /** 方法调用处理接口. */
    private var mPlugin: EcosedPlugin? = null

    /** 方法名. */
    private var mMethod: String? = null

    /** 参数Bundle. */
    private var mBundle: Bundle? = null

    /** 返回结果. */
    private var mResult: Any? = null

    /**
     * 设置方法调用.
     * @param handler 执行方法时调用EcosedMethodCallHandler.
     */
    internal fun setMethodCallHandler(handler: EcosedPlugin) {
        mPlugin = handler
    }

    /**
     * 获取上下文.
     * @return Context.
     */
    internal fun getContext(): Context {
        return mBinding.getContext()
    }

    /**
     * 是否调试模式.
     * @return Boolean.
     */
    internal fun isDebug(): Boolean {
        return mBinding.isDebug()
    }

    /**
     * 获取通道.
     * @return 通道名称.
     */
    fun getChannel(): String {
        return mChannel
    }

    /**
     * 执行方法回调.
     * @param name 通道名称.
     * @param method 方法名称.
     * @return 方法执行后的返回值.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> execMethodCall(name: String, method: String?, bundle: Bundle?): T? {
        mMethod = method
        mBundle = bundle
        if (name == mChannel) {
            mPlugin?.onEcosedMethodCall(
                call = call,
                result = result
            )
        }
        return mResult as T?
    }

    /** 用于调用方法的接口. */
    private val call: EcosedMethodCall = object : EcosedMethodCall {

        /**
         * 要调用的方法名.
         */
        override val method: String?
            get() = mMethod

        /**
         * 要传入的参数.
         */
        override val bundle: Bundle?
            get() = mBundle
    }

    /** 方法调用结果回调. */
    private val result: EcosedResult = object : EcosedResult {

        /**
         * 处理成功结果.
         */
        override fun success(result: Any?) {
            mResult = result
        }

        /**
         * 处理错误结果.
         */
        override fun error(
            errorCode: String,
            errorMessage: String?,
            errorDetails: Any?
        ): Nothing = error(
            message = "错误代码:$errorCode\n" +
                    "错误消息:$errorMessage\n" +
                    "详细信息:$errorDetails"
        )

        /**
         * 处理对未实现方法的调用.
         */
        override fun notImplemented() {
            mResult = null
        }
    }

//    /**
//     * 用于调用方法的接口.
//     */
//    interface MethodCall {
//
//        /**
//         * 要调用的方法名.
//         */
//        val method: String?
//
//        /**
//         * 要传入的参数.
//         */
//        val bundle: Bundle?
//    }

    /**
     * 方法调用处理接口.
     */
    interface MethodCallHandler {

        /**
         * 处理指定方法调用.
         * @param call 指定调用的方法.
         * @param result 方法调用结果回调.
         */
        fun onEcosedMethodCall(call: EcosedMethodCall, result: EcosedResult)
    }

//    /**
//     * 方法调用结果回调.
//     */
//    interface Result {
//
//        /**
//         * 处理成功结果.
//         * @param result 处理成功结果,注意可能为空.
//         */
//        fun success(result: Any?)
//
//        /**
//         * 处理错误结果.
//         * @param errorCode 错误代码.
//         * @param errorMessage 错误消息,注意可能为空.
//         * @param errorDetails 详细信息,注意可能为空.
//         */
//        fun error(
//            errorCode: String,
//            errorMessage: String?,
//            errorDetails: Any?
//        ): Nothing
//
//        /**
//         * 处理对未实现方法的调用.
//         */
//        fun notImplemented()
//    }
}