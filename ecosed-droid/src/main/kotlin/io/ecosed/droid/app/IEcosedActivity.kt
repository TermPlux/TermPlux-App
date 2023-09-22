package io.ecosed.droid.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable

interface IEcosedActivity {

    /**
     * 将EcosedDroid附加到Activity
     * @param activity 要附加的Activity
     */
    fun IEcosedActivity.attachEcosed(activity: ComponentActivity)

    /**
     * 将EcosedDroid与Activity分离
     */
    fun IEcosedActivity.detachEcosed()

    /**
     * 设置布局,EcosedLauncher标记为true则设置主页布局,false为整个Activity的布局.
     * @param content 页面布局
     */
    fun IEcosedActivity.setContentComposable(content: @Composable () -> Unit)

    /**
     * 调用插件代码的方法.
     * @param name 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值.
     */
    fun <T> IEcosedActivity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?
    ): T?

    fun IEcosedActivity.toast(obj: Any)
    fun IEcosedActivity.log(obj: Any)
    fun IEcosedActivity.openUrl(url: String)
    fun IEcosedActivity.openApp(packageName: String)
    fun IEcosedActivity.isInstallApp(packageName: String): Boolean

}