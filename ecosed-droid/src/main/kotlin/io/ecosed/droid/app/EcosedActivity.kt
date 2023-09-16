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
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.color.DynamicColors
import io.ecosed.droid.R
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.plugin.PluginExecutor
import io.ecosed.droid.ui.layout.ActivityMain
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.utils.ThemeHelper

class EcosedActivity<YourActivity : Activity> : ContextWrapper(null), EcosedActivityImpl,
    DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private var layoutResId = -1
    private var isDebug = false

    private lateinit var mME: YourActivity

    override fun Activity.attachEcosed(activity: Activity, lifecycle: Lifecycle) {
        attachBaseContext(activity.baseContext)
        mActivity = activity
        @Suppress("UNCHECKED_CAST")
        mME = mActivity as YourActivity
        lifecycle.addObserver(observer = this@EcosedActivity)
    }

    override fun Activity.detachEcosed(lifecycle: Lifecycle) {
        lifecycle.removeObserver(observer = this@EcosedActivity)
    }

    override fun <T> Activity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?,
    ): T? {
        return PluginExecutor.execMethodCall<T>(
            activity = mActivity,
            name = name,
            method = method,
            bundle = bundle
        )
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onCreate(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {
        when {
            isLaunchMode() -> if (this@lifecycleUnit is AppCompatActivity) {

                when {
                    execMethodCall<Boolean>(
                        name = LibEcosedPlugin.channel,
                        method = LibEcosedPlugin.isDebug,
                        bundle = null
                    ) == true -> isDebug = true
                }

                setTheme(R.style.Theme_LibEcosed)
                DynamicColors.applyToActivityIfAvailable(this)




                setContent {
                    LibEcosedTheme(
                        dynamicColor = ThemeHelper.isUsingSystemColor()
                    ) { dynamic ->
                        ActivityMain(
                            windowSize = calculateWindowSizeClass(
                                activity = this@lifecycleUnit
                            ),
                            displayFeatures = calculateDisplayFeatures(
                                activity = this@lifecycleUnit
                            ),
                            productLogo = execMethodCall<Drawable>(
                                name = LibEcosedPlugin.channel,
                                method = LibEcosedPlugin.getProductLogo,
                                bundle = null
                            ),
                            topBarVisible = true,
                            topBarUpdate = { toolbar ->
                                setSupportActionBar(
                                    toolbar
                                )
                            },
                            viewPager2 = ViewPager2(this),
                            androidVersion = "13",
                            shizukuVersion = "13",
                            current = {
                                //mViewPager2.currentItem = it
                            },
                            toggle = {
                                //toggle()
                            },
                            taskbar = {
                                Taskbar.openSettings(
                                    this@lifecycleUnit,
                                    getString(R.string.taskbar_title),
                                    when (dynamic){
                                        true -> R.style.Theme_LibEcosed_TaskbarDynamic
                                        false -> R.style.Theme_LibEcosed_Taskbar
                                    }
                                )
                            },
                            customTabs = { url ->
                                CustomTabsIntent.Builder()
                                    .build()
                                    .launchUrl(
                                        this@lifecycleUnit,
                                        Uri.parse(url)
                                    )
                            }
                        )
                    }
                }


                if (isDebug) AlertDialog.Builder(this@lifecycleUnit).apply {

                    setIcon(
                        execMethodCall<Drawable>(
                            name = LibEcosedPlugin.channel,
                            method = LibEcosedPlugin.getProductLogo,
                            bundle = null
                        )
                    )
                    setTitle(AppUtils.getAppName())
                    setMessage("请在标记为EcosedLauncher的Activity的AndroidManifest中添加HOME属性。本提示仅在DEBUG模式中弹出，不影响应用发布之后正常使用。")
                    setPositiveButton("我知道了", null)
                    setNegativeButton("查看文档") { _, _ ->
                        CustomTabsIntent.Builder()
                            .build()
                            .launchUrl(
                                this@lifecycleUnit,
                                Uri.parse("https://github.com/ecosed/EcosedDroid/wiki")
                            )
                    }
                    create()
                    show()
                }
            } else error("错误: @EcosedLauncher需要继承AppCompatActivity或其子类!")

            else -> {
                setLayout()


            }
        }
    }

    override fun onStart(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onStart(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onResume(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onResume(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onPause(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onPause(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onStop(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onStop(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onDestroy(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onDestroy(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"

    private fun lifecycleUnit(
        superUnit: (() -> Unit) -> Unit,
        content: Activity.() -> Unit,
    ) {
        mActivity.apply {
            superUnit {
                content()
            }
        }
    }

    private fun <T> defaultUnit(
        content: Activity.() -> T,
    ): T {
        return mActivity.content()
    }

    private fun isDefaultHome(): Boolean = defaultUnit {
        var isHome = false
        intent.categories.forEach {
            if (it == Intent.CATEGORY_HOME) {
                isHome = true
            }
        }
        return@defaultUnit isHome
    }

    private fun isLaunchMode(): Boolean = defaultUnit {
        try {
            val isLauncher = javaClass.getAnnotation(EcosedLauncher::class.java)
            return@defaultUnit isLauncher != null && isLauncher.isLauncher
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@defaultUnit false
    }

    @SuppressLint("DiscouragedApi")
    private fun setLayout() = defaultUnit {
        layoutResId = resources.getIdentifier(guessNameOfLayoutResId(), "layout", packageName)
        if (layoutResId != 0) {
            mActivity.setContentView(layoutResId)
        } else {
            return@defaultUnit
        }
    }

    private fun guessNameOfLayoutResId(): String = defaultUnit {
        val words = javaClass.simpleName.split("(?<!^)(?=[A-Z])".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()
        val stringBuffer = StringBuffer(words.size)
        for (i in words.indices.reversed()) {
            stringBuffer.append(words[i].lowercase())
            if (i != 0) stringBuffer.append("_")
        }
        return@defaultUnit stringBuffer.toString()
    }
}