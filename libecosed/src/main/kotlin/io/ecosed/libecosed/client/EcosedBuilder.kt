package io.ecosed.libecosed.client

import android.content.Context

internal class EcosedBuilder {

    /** 上下文 */
    private lateinit var mContext: Context

    /**
     * 传入上下文
     * @param context 上下文
     */
    internal fun init(
        context: Context?
    ): EcosedBuilder {
        context?.let {
            mContext = it
        }
        return this@EcosedBuilder
    }

    /**
     * 构建API
     */
    internal fun build(
        content: EcosedClient.() -> Unit
    ) {
        content(EcosedClient(context = mContext))
    }
}