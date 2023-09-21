package io.ecosed.droid.app

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle

interface IEcosedActivity {

    /**
     * 将EcosedDroid附加到Activity
     * @param activity 要附加的Activity
     * @param lifecycle Activity的生命周期
     */
    fun Activity.attachEcosed(
        activity: Activity,
        lifecycle: Lifecycle
    )

    fun Activity.onBack(
        back: () -> Unit
    )

    fun Activity.detachEcosed()


    fun ComponentActivity.setContentComposable(content: @Composable () -> Unit)



    /**
     * 调用插件代码的方法.
     * @param name 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值.
     */
    fun <T> Activity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?
    ): T?
}