package io.ecosed.droid.engine

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import io.ecosed.droid.app.EcosedApplicationImpl
import io.ecosed.droid.app.EcosedPlugin
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginBinding

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件引擎
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
class EcosedEngine {

    /** 应用程序全局类. */
    private lateinit var mApp: Application

    /** 基础上下文. */
    private lateinit var mBase: Context

    /** 客户端组件. */
    private lateinit var mClient: EcosedClient

    /** 应用程序全局上下文, 非UI上下文. */
    private lateinit var mContext: Context

    /** 插件绑定器. */
    private var mBinding: PluginBinding? = null

    /** 插件列表. */
    private var mPluginList: ArrayList<EcosedPlugin>? = null

    /**
     * 将引擎附加到应用.
     */
    private fun attach() {
        when {
            (mPluginList == null) or (mBinding == null) -> apply {
                // 客户端组件第一次初始化, 附加基本上下文.
                mClient.firstAttach(base = mBase)
                // 初始化插件绑定器.
                mBinding = PluginBinding(
                    context = mContext,
                    client = mClient,
                    debug = mClient.isDebug(),
                    libEcosed = mClient.getLibEcosed(),
                    logo = mClient.getProductLogo()
                )
                // 初始化插件列表.
                mPluginList = arrayListOf()
                // 加载客户端组件
                mBinding?.let { binding ->
                    mClient.apply {
                        try {
                            onEcosedAdded(binding = binding)
                            if (mClient.isDebug()) {
                                Log.d(tag, "客户端组件已加载")
                            }
                        } catch (e: Exception) {
                            if (mClient.isDebug()) {
                                Log.e(tag, "客户端组件加载失败!", e)
                            }
                        }
                    }
                }.run {
                    mPluginList?.add(element = mClient)
                    if (mClient.isDebug()) {
                        Log.d(tag, "客户端组件已添加到插件列表")
                    }
                }
                // 加载LibEcosed框架 (如果使用了的话).
                mClient.getLibEcosed()?.let { ecosed ->
                    mBinding?.let { binding ->
                        ecosed.apply {
                            try {
                                attach(base = mBase)
                                if (mClient.isDebug()) {
                                    Log.d(tag, "LibEcosed框架已附加基本上下文")
                                }
                                init()
                                if (mClient.isDebug()) {
                                    Log.d(tag, "LibEcosed框架已初始化")
                                }
                                synchronized(ecosed) {
                                    initSDKs(application = mApp)
                                    initSDKInitialized()
                                }
                                if (mClient.isDebug()) {
                                    Log.d(tag, "LibEcosed框架已初始化SDK")
                                }
                                onEcosedAdded(binding = binding)
                                if (mClient.isDebug()) {
                                    Log.d(tag, "LibEcosed框架已加载")
                                }
                            } catch (e: Exception) {
                                if (mClient.isDebug()) {
                                    Log.e(tag, "LibEcosed框架加载失败!", e)
                                }
                            }
                        }
                    }.run {
                        mPluginList?.add(element = ecosed)
                        if (mClient.isDebug()) {
                            Log.d(tag, "LibEcosed框架已添加到插件列表")
                        }
                    }
                }
                // 添加所有插件.
                mClient.getPluginList()?.let { plugins ->
                    mBinding?.let { binding ->
                        plugins.forEach { plugin ->
                            plugin.apply {
                                try {
                                    onEcosedAdded(binding = binding)
                                    if (mClient.isDebug()) {
                                        Log.d(tag, "插件${plugin.javaClass.name}已加载")
                                    }
                                } catch (e: Exception) {
                                    if (mClient.isDebug()) {
                                        Log.e(tag, "插件添加失败!", e)
                                    }
                                }
                            }
                        }
                    }.run {
                        plugins.forEach { plugin ->
                            mPluginList?.add(element = plugin)
                            if (mClient.isDebug()) {
                                Log.d(tag, "插件${plugin.javaClass.name}已添加到插件列表")
                            }
                        }
                    }
                }
            }

            else -> if (mClient.isDebug()) {
                Log.e(tag, "请勿重复执行attach!")
            }
        }
    }

    /**
     * 调用插件代码的方法.
     * @param name 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值,类型为Any?.
     */
    internal fun <T> execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?
    ): T? {
        var result: T? = null
        try {
            mPluginList?.forEach { plugin ->
                plugin.getPluginChannel.let { channel ->
                    when (channel.getChannel()) {
                        name -> {
                            result = channel.execMethodCall<T>(
                                name = name,
                                method = method,
                                bundle = bundle
                            )
                            if (mClient.isDebug()) {
                                Log.d(
                                    tag,
                                    "插件代码调用成功!\n" +
                                            "通道名称:${name}.\n" +
                                            "方法名称:${method}.\n" +
                                            "返回结果:${result}."
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (mClient.isDebug()) {
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

    companion object : Builder {

        /** 用于打印日志的标签. */
        private const val tag: String = "PluginEngine"

        /**
         * 引擎构建函数.
         * @param application 传入Application.
         * @return 返回已构建的引擎.
         */
        override fun create(
            application: Application
        ): EcosedEngine = EcosedEngine().let { engine ->
            return@let engine.apply {
                if (application is EcosedApplicationImpl) {
                    application.apply {
                        mApp = application
                        mBase = baseContext
                        mContext = applicationContext
                        mClient = getEcosedClient()
                    }.run {
                        attach()
                    }
                } else error(
                    message = "错误:EcosedApplication接口未实现.\n" +
                            "提示1:可能未在应用的Application全局类实现EcosedApplication接口.\n" +
                            "提示2:应用的Application全局类可能未在AndroidManifest.xml中注册."
                )
            }
        }
    }
}