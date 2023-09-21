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
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.farmerbb.taskbar.lib.Taskbar
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.material.internal.EdgeToEdgeUtils
import io.ecosed.droid.R
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.plugin.PluginExecutor
import io.ecosed.droid.ui.layout.ActivityMain
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.utils.ThemeHelper
import io.ecosed.droid.utils.TopAppBarUtils

class EcosedActivity<YourActivity : Activity> : ContextWrapper(null), IEcosedActivity,
    LifecycleOwner, DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private var layoutResId = -1
    private var isDebug = false


    private lateinit var mViewPager2: ViewPager2


    private lateinit var mME: YourActivity

    private lateinit var mLifecycle: Lifecycle
    private lateinit var delayHideTouchListener: View.OnTouchListener
    private lateinit var toggle: () -> Unit
    private var actionBarVisible: Boolean by mutableStateOf(value = true)


    @Suppress(names = ["UNCHECKED_CAST"])
    override fun Activity.attachEcosed(
        activity: Activity,
        lifecycle: Lifecycle
    ) {
        attachBaseContext(activity.baseContext)
        mActivity = activity
        mLifecycle = lifecycle
        mME = mActivity as YourActivity
        this@EcosedActivity.lifecycle.addObserver(observer = this@EcosedActivity)
    }



    override fun Activity.detachEcosed() {
        this@EcosedActivity.lifecycle.removeObserver(observer = this@EcosedActivity)
    }

    override fun ComponentActivity.setContentComposable(
        content: @Composable () -> Unit
    ) {

    }

    override fun Activity.onBack(
        back: () -> Unit
    ) = defaultUnit {
        if (!isDefaultHome()) {
            back()
        } else {
            // 切换到应用页
        }
    }

    override fun <T> Activity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?,
    ): T? {
        return PluginExecutor.execMethodCall<T>(
            activity = mME,
            name = name,
            method = method,
            bundle = bundle
        )
    }

    override val lifecycle: Lifecycle
        get() = mLifecycle

    @SuppressLint(value = ["RestrictedApi"])
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onCreate(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {
        when {
            isLaunchMode() -> when (this@hasSuperUnit) {
                is ComponentActivity -> (this@hasSuperUnit as ComponentActivity).apply {
                    // 设置主题
                    setTheme(R.style.Theme_LibEcosed)
                    // 启用边倒边
                    EdgeToEdgeUtils.applyEdgeToEdge(window, true)
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


                    TopAppBarUtils.newInstance().attach { visible, onTouchListener, function ->
                        actionBarVisible = visible
                        delayHideTouchListener = onTouchListener
                        toggle = function
                    }

//                    mViewPager2 = ViewPager2(this).apply {
//                        isUserInputEnabled = true
//                        orientation = ViewPager2.ORIENTATION_HORIZONTAL
//                  //      adapter = PagerAdapter(activity = this)
//                        offscreenPageLimit = (adapter as PagerAdapter).itemCount
//
//
//                        //  setPageTransformer(PageTransformerUtils())
//                    }

                    when {
                        execMethodCall<Boolean>(
                            name = LibEcosedPlugin.channel,
                            method = LibEcosedPlugin.isDebug,
                            bundle = null
                        ) == true -> isDebug = true
                    }




                    setContent {
                        LocalView.current.setOnTouchListener(delayHideTouchListener)
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
                                productLogo = execMethodCall<Drawable>(
                                    name = LibEcosedPlugin.channel,
                                    method = LibEcosedPlugin.getProductLogo,
                                    bundle = null
                                ),
                                topBarVisible = actionBarVisible,
                                topBarUpdate = { toolbar ->

                                },
                                viewPager2 = ViewPager2(this),
                                androidVersion = "13",
                                shizukuVersion = "13",
                                current = {
                                    //mViewPager2.currentItem = it
                                },
                                toggle = {
                                    toggle()
                                },
                                taskbar = {
                                    Taskbar.openSettings(
                                        this@hasSuperUnit,
                                        getString(R.string.taskbar_title),
                                        when (dynamic) {
                                            true -> R.style.Theme_LibEcosed_TaskbarDynamic
                                            false -> R.style.Theme_LibEcosed_Taskbar
                                        }
                                    )
                                },
                                launchUrl = { url ->
                                    CustomTabsIntent.Builder()
                                        .build()
                                        .launchUrl(
                                            this@hasSuperUnit,
                                            Uri.parse(url)
                                        )
                                }
                            )
                        }
                    }
                }

                else -> error(
                    message = "错误: @EcosedLauncher需要继承ComponentActivity或其子类!"
                )
            }


            else -> {
                setLayout()


            }
        }
    }

    override fun onStart(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onStart(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onResume(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onResume(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onPause(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onPause(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onStop(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onStop(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    override fun onDestroy(owner: LifecycleOwner) = hasSuperUnit(
        superUnit = { content ->
            super.onDestroy(
                owner = owner
            ).apply {
                content()
            }
        }
    ) {

    }

    private fun hasSuperUnit(
        superUnit: (() -> Unit) -> Unit,
        content: YourActivity.() -> Unit,
    ): Unit = superUnit {
        mME.content()
    }

    private fun <T> defaultUnit(
        content: YourActivity.() -> T,
    ): T = mME.content()


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
        if (layoutResId != 0) setContentView(layoutResId)
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

    private companion object {
        const val tag: String = "EcosedActivity"

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = false

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300
    }
}