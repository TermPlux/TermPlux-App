package io.ecosed.droid.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

interface IEcosedActivity {

    /**
     * 将EcosedDroid附加到Activity
     * @param activity 要附加的Activity
     */
    fun IEcosedActivity.attachEcosed(activity: ComponentActivity)

    fun IEcosedActivity.detachEcosed()

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