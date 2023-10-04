/**
 * Copyright EcosedDroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ecosed.droid.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ServiceUtils
import io.ecosed.droid.client.EcosedClient
import io.ecosed.droid.engine.EcosedEngine
import io.ecosed.droid.service.EcosedService

class EcosedActivity<YourApplication : IEcosedApplication, YourActivity : IEcosedActivity> :
    ContextWrapper(null), IEcosedActivity, LifecycleOwner, DefaultLifecycleObserver {


    private lateinit var mActivity: Activity
    private lateinit var mApplication: Application
    private lateinit var mLifecycle: Lifecycle
    private lateinit var mYourActivity: YourActivity
    private lateinit var mYourApplication: YourApplication


    private var isDebug = false
    private var isLaunch = false

    private var mToast: Toast? = null


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
//        val run = ServiceUtils.isServiceRunning(EcosedService().javaClass)
//        Toast.makeText(this@EcosedActivity, run.toString(), Toast.LENGTH_SHORT).show()

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun IEcosedActivity.attachEcosed(
        activity: Activity,
        lifecycle: Lifecycle,
    ) {
        // 附加基本上下文
        attachBaseContext(base = activity)
        // 获取Activity
        mActivity = activity
        // 获取Application
        mApplication = activity.application
        // 获取生命周期
        mLifecycle = lifecycle
        // 获取传入的Activity
        @Suppress(names = ["UNCHECKED_CAST"])
        mYourActivity = mActivity as YourActivity
        // 获取传入的Application
        @Suppress(names = ["UNCHECKED_CAST"])
        mYourApplication = mApplication as YourApplication
        // 如果传入错误的类则抛出异常
        when {
            mYourActivity !is Activity -> error(
                message = errorActivityExtends
            )
            mYourApplication !is Application -> error(
                message = errorApplicationExtends
            )
        }
        // 获取是否调试模式
        execMethodCall<Boolean>(
            channel = EcosedClient.mChannelName,
            method = EcosedClient.mMethodDebug
        )?.let { debug ->
            isDebug = debug
        }


        this@EcosedActivity.lifecycle.addObserver(this@EcosedActivity)

    }

    override fun IEcosedActivity.detachEcosed() {

    }

    override fun <T> IEcosedActivity.execMethodCall(
        channel: String,
        method: String,
        bundle: Bundle?,
    ): T? = defaultUnit {
        return@defaultUnit engineUnit {
            return@engineUnit execMethodCall<T>(
                channel = channel,
                method = method,
                bundle = bundle
            )
        }
    }


    override fun IEcosedActivity.toast(obj: Any) = defaultUnit<Unit> {
        try {
            runOnUiThread {
                if (mToast == null) {
                    mToast = Toast.makeText(
                        this@EcosedActivity,
                        mNull,
                        Toast.LENGTH_SHORT
                    )
                }
                mToast?.setText(obj.toString())
                mToast?.show()
            }
        } catch (e: Exception) {
            Log.e(tag, "toast", e)
        }
    }

    override fun IEcosedActivity.log(obj: Any) = defaultUnit<Unit> {
        Log.i(tag, obj.toString())
    }

    override fun IEcosedActivity.openUrl(url: String) = defaultUnit {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                this@defaultUnit,
                Uri.parse(url)
            )
    }

    override fun IEcosedActivity.openApp(packageName: String) = defaultUnit {
        AppUtils.launchApp(packageName)
    }

    override fun IEcosedActivity.isInstallApp(packageName: String): Boolean = defaultUnit {
        return@defaultUnit AppUtils.isAppInstalled(packageName)
    }

    override val lifecycle: Lifecycle
        get() = mLifecycle

    private fun hasSuperUnit(
        superUnit: (() -> Unit) -> Unit,
        content: Activity.() -> Unit,
    ): Unit = superUnit {
        when (mYourActivity) {
            is Activity -> {
                (mYourActivity as Activity).content()
            }

            else -> error(
                message = errorActivityExtends
            )
        }
    }

    private fun <T> engineUnit(
        content: EcosedEngine.() -> T,
    ): T? = (mYourApplication.getEngine as EcosedEngine).content()

    private fun <T> defaultUnit(
        content: Activity.() -> T,
    ): T = when (mYourActivity) {
        is Activity -> {
            (mYourActivity as Activity).content()
        }

        else -> error(
            message = errorActivityExtends
        )
    }

    private companion object {
        const val tag: String = "EcosedActivity"
        const val mNull: String = ""
        const val errorActivityExtends: String = "错误: EcosedActivity只能在Activity中使用!"
        const val errorApplicationExtends: String = "错误: EcosedApplication只能在Application中使用!"
    }
}