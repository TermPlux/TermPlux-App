package io.ecosed.libecosed.plugin

import android.content.Context
import android.graphics.drawable.Drawable

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 插件绑定器
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
class PluginBinding constructor(
    context: Context?,
    client: EcosedClient,
    debug: Boolean,
    libEcosed: LibEcosed?,
    logo: Drawable?
) {

    /** 应用程序全局上下文. */
    private val mContext: Context? = context

    /** 客户端组件. */
    private val mClient: EcosedClient = client

    /** 是否调试模式. */
    private val mDebug: Boolean = debug

    /** LibEcosed. */
    private val mLibEcosed: LibEcosed? = libEcosed

    /** 产品图标. */
    private val mLogo: Drawable? = logo

    /**
     * 获取上下文.
     * @return Context.
     */
    internal fun getContext(): Context? {
        return mContext
    }

    /**
     * 是否调试模式.
     * @return Boolean.
     */
    internal fun isDebug(): Boolean {
        return mDebug
    }

    /**
     * 获取客户端组件 - LibEcosed框架专用接口.
     * @param ecosed 用于判断是否是LibEcosed.
     * @return EcosedClient?.
     */
    internal fun getClient(ecosed: LibEcosed): EcosedClient? {
        return when (ecosed.javaClass) {
            mLibEcosed?.javaClass -> mClient
            else -> null
        }
    }

    /**
     * 获取产品图标 - LibEcosed框架专用接口.
     * @param ecosed 用于判断是否是LibEcosed.
     * @return Drawable?.
     */
    internal fun getProductLogo(ecosed: LibEcosed): Drawable? {
        return when (ecosed.javaClass) {
            mLibEcosed?.javaClass -> mLogo
            else -> null
        }
    }
}