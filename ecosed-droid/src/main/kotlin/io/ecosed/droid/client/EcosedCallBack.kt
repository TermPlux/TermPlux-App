package io.ecosed.droid.client

internal interface EcosedCallBack {

    /** 在服务绑定成功时回调 */
    fun onEcosedConnected()

    /** 在服务解绑或意外断开链接时回调 */
    fun onEcosedDisconnected()

    /** 在服务端服务未启动时绑定服务时回调 */
    fun onEcosedDead()

    /** 在未绑定服务状态下调用API时回调 */
    fun onEcosedUnbind()
}