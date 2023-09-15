package io.ecosed.droid

import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginEngine

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 应用程序接口
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
interface EcosedApplication {

    /** 获取插件引擎. */
    fun getPluginEngine(): PluginEngine

    /** 获取应用程序主机. */
    fun getEcosedClient(): EcosedClient
}