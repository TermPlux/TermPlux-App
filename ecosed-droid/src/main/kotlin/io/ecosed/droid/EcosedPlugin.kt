package io.ecosed.droid

import io.ecosed.droid.plugin.PluginBinding
import io.ecosed.droid.plugin.PluginChannel

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件接口
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
interface EcosedPlugin {

    /** 插件被添加时执行. */
    fun onEcosedAdded(binding: PluginBinding)

    /** 获取插件通信通道. */
    val getPluginChannel: PluginChannel
}