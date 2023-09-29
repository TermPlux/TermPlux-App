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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.internal.EdgeToEdgeUtils
import io.ecosed.droid.R
import io.ecosed.droid.engine.EcosedEngine
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.ui.layout.ActivityMain
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.utils.ThemeHelper
import io.ecosed.droid.utils.TopAppBarUtils

class EcosedActivity<YourApplication : IEcosedApp, YourActivity : IEcosedActivity> :
    ContextWrapper(null), IEcosedActivity, EcosedActivityContent, LifecycleOwner, DefaultLifecycleObserver {






    private lateinit var mActivity: Activity
    private lateinit var mApplication: Application
    private lateinit var mLifecycle: Lifecycle
    private lateinit var mYourActivity: YourActivity
    private lateinit var mYourApplication: YourApplication


    private var isDebug = false
    private var isLaunch = false

    private var mParent: () -> Unit = {}
    private var mBody: () -> Unit = {}


    private lateinit var toggle: () -> Unit
    private var isVisible: Boolean by mutableStateOf(value = true)


    private var mShowUi: Boolean by mutableStateOf(value = false)

    private val mContent: MutableState<@Composable () -> Unit> = mutableStateOf<(@Composable () -> Unit)>(
        value = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "页面为空"
                )
            }
        }
    )


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mShowUi = !mShowUi
    }






    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @SuppressLint("RestrictedApi")
    override fun IEcosedActivity.onAttachEcosed(
        activity: ComponentActivity,
        content: EcosedActivityContent.() -> Unit
    ) {


        hasSuperUnit(
            superUnit = { sub ->
                attachBaseContext(base = activity.baseContext)
                content.invoke(this@EcosedActivity)
                mActivity = activity
                mApplication = activity.application
                mLifecycle = activity.lifecycle
                @Suppress("UNCHECKED_CAST")
                mYourActivity = mActivity as YourActivity
                @Suppress("UNCHECKED_CAST")
                mYourApplication = mApplication as YourApplication
                isDebug()?.let { isDebug = it }
                mParent()
                sub()
                mBody()
                mParent = {}
                mBody = {}

                lifecycle.addObserver(this@EcosedActivity)
            }
        ) {







            when {
                isLaunch -> {
                    // 设置主题
                    setTheme(R.style.Theme_LibEcosed)
                    // 启用边倒边
                    EdgeToEdgeUtils.applyEdgeToEdge(window, true)
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)





                    setContent {
                        val view = LocalView.current
                        TopAppBarUtils.build().attach { visible, onTouchListener, function ->
                            isVisible = visible
                            view.setOnTouchListener(onTouchListener)
                            toggle = function
                        }
                        LibEcosedTheme(
                            dynamicColor = ThemeHelper.isUsingSystemColor()
                        ) { dynamic ->
                            ActivityMain(
                                windowSize = calculateWindowSizeClass(
                                    activity = this@hasSuperUnit
                                ),
                                displayFeatures = calculateDisplayFeatures(
                                    activity = this@hasSuperUnit
                                ),
                                topBarVisible = isVisible,
                                uiVisible = mShowUi,
                                topBarUpdate = { toolbar ->

                                },
                                content = mContent.value,
                                androidVersion = "13",
                                shizukuVersion = "13",
                                current = {
                                },
                                toggle = {
                                    toggle()
                                },
                                taskbar = {

                                },
                                launchUrl = { url ->
                                    openUrl(url = url)
                                }
                            )
                        }
                    }

                }


                else -> {
                    // setLayout()


                }
            }
        }






    }

    override fun IEcosedActivity.onDetachEcosed(
        content: EcosedActivityContent.() -> Unit
    ) {
        content.invoke(this@EcosedActivity)
        mParent()
        mBody()
        mParent = {}
        mBody = {}
    }

    override fun IEcosedActivity.setContentComposable(
        content: @Composable () -> Unit,
    ) = defaultUnit {
        runOnUiThread {
            when {
                isLaunch -> mContent.value = content
                else -> setContent(content = content)
            }
        }
    }

    override fun <T> IEcosedActivity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?,
    ): T? = defaultUnit {
        return@defaultUnit engineUnit {
            return@engineUnit execMethodCall<T>(
                name = name,
                method = method,
                bundle = bundle
            )
        }
    }

    override fun IEcosedActivity.toast(obj: Any) = defaultUnit {
        try {
            runOnUiThread {
                Toast.makeText(
                    this@EcosedActivity,
                    obj.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    override var parent: (() -> Unit)?
        get() = null
        set(value) {
            value?.let {
                mParent = it
            }
        }

    override var isLauncher: Boolean?
        get() = isLaunch
        set(value) {
            value?.let {
                isLaunch = it
            }
        }

    override var body: (() -> Unit)?
        get() = null
        set(value) {
            value?.let {
                mBody = it
            }
        }

    override val lifecycle: Lifecycle
        get() = mLifecycle














    private fun isDebug(): Boolean? = hostUnit {
        return@hostUnit isDebug()
    }

    private fun hasSuperUnit(
        superUnit: (() -> Unit) -> Unit,
        content: ComponentActivity.() -> Unit,
    ): Unit = superUnit {
        when (mYourActivity) {
            is ComponentActivity -> {
                (mYourActivity as ComponentActivity).content()
            }

            else -> error(
                message = errorActivityExtends
            )
        }
    }

    private fun <T> hostUnit(
        content: EcosedAppHost.() -> T
    ): T? = (mYourApplication.getHost as EcosedAppHost).content()

    private fun <T> engineUnit(
        content: EcosedEngine.() -> T
    ): T? = (mYourApplication.getEngine as EcosedEngine).content()

    private fun <T> defaultUnit(
        content: ComponentActivity.() -> T,
    ): T = when (mYourActivity) {
        is ComponentActivity -> {
            (mYourActivity as ComponentActivity).content()
        }


        else -> error(
            message = errorActivityExtends
        )
    }

    private companion object {
        const val tag: String = "EcosedActivity"

        const val errorActivityExtends: String = "错误: Activity需要继承ComponentActivity或其子类!"

    }
}