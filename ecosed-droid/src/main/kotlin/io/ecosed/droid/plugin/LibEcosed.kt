package io.ecosed.droid.plugin

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import io.ecosed.droid.app.EcosedPlugin

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: LibEcosed框架接口
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
abstract class LibEcosed : ContextWrapper(null), EcosedPlugin, PluginChannel.MethodCallHandler {

    /**
     * 附加基本上下文.
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    /**
     * 初始化.
     */
    abstract fun init()

    /**
     * 初始化SDK.
     */
    open fun initSDKs(application: Application) = Unit

    /**
     * 初始化完成.
     */
    open fun initSDKInitialized() = Unit

    /**
     * LibEcosed框架接口初始化 - 内部API.
     * @param base 通过引擎设置基础上下文.
     */
    internal fun attach(base: Context?) {
        attachBaseContext(base)
    }
}