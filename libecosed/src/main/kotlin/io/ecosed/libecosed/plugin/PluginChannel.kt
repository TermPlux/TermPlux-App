package io.ecosed.libecosed.plugin

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件通信通道
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
class PluginChannel constructor(binding: PluginBinding, channel: String) {

    /** 插件绑定器. */
    private var mBinding: PluginBinding = binding

    /** 插件通道. */
    private var mChannel: String = channel

    /** 方法调用处理接口. */
    private var mHandler: MethodCallHandler? = null

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
    fun setMethodCallHandler(handler: MethodCallHandler) {
        mHandler = handler
    }

    /**
     * 获取上下文.
     * @return Context.
     */
    fun getContext(): Context? {
        return mBinding.getContext()
    }

    /**
     * 是否调试模式.
     * @return Boolean.
     */
    fun isDebug(): Boolean {
        return mBinding.isDebug()
    }

    /**
     * 获取客户端组件 - LibEcosed框架专用接口.
     * @param ecosed 用于判断是否是LibEcosed.
     * @return EcosedClient?.
     */
    fun getClient(ecosed: LibEcosed): EcosedClient? {
        return mBinding.getClient(ecosed = ecosed)
    }

    /**
     * 获取产品标志 - LibEcosed框架专用接口.
     * @param ecosed 用于判断是否是LibEcosed.
     * @return Drawable?.
     */
    fun getProductLogo(ecosed: LibEcosed): Drawable? {
        return mBinding.getProductLogo(ecosed = ecosed)
    }

    /**
     * 获取通道.
     * @return 通道名称.
     */
    internal fun getChannel(): String {
        return mChannel
    }

    /**
     * 执行方法回调.
     * @param name 通道名称.
     * @param method 方法名称.
     * @return 方法执行后的返回值.
     */
    internal fun <T> execMethodCall(name: String, method: String?, bundle: Bundle?): T? {
        mMethod = method
        mBundle = bundle
        if (name == mChannel) {
            mHandler?.onEcosedMethodCall(
                call = call,
                result = result
            )
        }
        @Suppress("UNCHECKED_CAST")
        return mResult as T?
    }

    /** 用于调用方法的接口. */
    private val call: MethodCall = object : MethodCall {

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
    private val result: Result = object : Result {

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

    /**
     * 用于调用方法的接口.
     */
    interface MethodCall {

        /**
         * 要调用的方法名.
         */
        val method: String?

        /**
         * 要传入的参数.
         */
        val bundle: Bundle?
    }

    /**
     * 方法调用处理接口.
     */
    interface MethodCallHandler {

        /**
         * 处理指定方法调用.
         * @param call 指定调用的方法.
         * @param result 方法调用结果回调.
         */
        fun onEcosedMethodCall(call: MethodCall, result: Result)
    }

    /**
     * 方法调用结果回调.
     */
    interface Result {

        /**
         * 处理成功结果.
         * @param result 处理成功结果,注意可能为空.
         */
        fun success(result: Any?)

        /**
         * 处理错误结果.
         * @param errorCode 错误代码.
         * @param errorMessage 错误消息,注意可能为空.
         * @param errorDetails 详细信息,注意可能为空.
         */
        fun error(
            errorCode: String,
            errorMessage: String?,
            errorDetails: Any?
        ): Nothing

        /**
         * 处理对未实现方法的调用.
         */
        fun notImplemented()
    }
}