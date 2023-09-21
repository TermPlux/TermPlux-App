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
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import io.ecosed.droid.engine.EcosedEngine
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.ui.layout.ActivityMain
import io.ecosed.droid.ui.theme.LibEcosedTheme
import io.ecosed.droid.utils.ThemeHelper
import io.ecosed.droid.utils.TopAppBarUtils

class EcosedActivity<YourApplication : IEcosedApplication, YourActivity : IEcosedActivity> :
    ContextWrapper(null), IEcosedActivity, LifecycleOwner, DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private lateinit var mApplication: Application
    private lateinit var mLifecycle: Lifecycle

    private lateinit var mME: YourActivity
    private lateinit var mAPP: YourApplication


    private var layoutResId = -1

    private var isDebug = false
    private var isLaunch = false
    private lateinit var mProductLogo: Drawable


    private lateinit var mViewPager2: ViewPager2

    private lateinit var delayHideTouchListener: View.OnTouchListener
    private lateinit var toggle: () -> Unit
    private var isVisible: Boolean by mutableStateOf(value = true)
    private val content = mutableStateOf<(@Composable () -> Unit)?>(value = null)


    @Suppress(names = ["UNCHECKED_CAST"])
    override fun IEcosedActivity.attachEcosed(
        activity: ComponentActivity,
        lifecycle: Lifecycle,
    ) {
        attachBaseContext(activity.baseContext)
        mActivity = activity
        mApplication = activity.application
        mLifecycle = lifecycle
        mME = mActivity as YourActivity
        mAPP = mApplication as YourApplication
        this@EcosedActivity.lifecycle.addObserver(observer = this@EcosedActivity)
    }

    override fun IEcosedActivity.detachEcosed() {
        this@EcosedActivity.lifecycle.removeObserver(observer = this@EcosedActivity)
    }

    override fun IEcosedActivity.setContentComposable(
        content: @Composable () -> Unit,
    ) = defaultUnit {
        if (isLaunch) {
            this@EcosedActivity.content.value = content
        } else {
            setContent(content = content)
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

    override fun IEcosedActivity.log(obj: Any) = defaultUnit {

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

        execMethodCall<Boolean>(
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.isDebug,
            bundle = null
        )?.let { debug ->
            isDebug = debug
        }

        execMethodCall<Drawable>(
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.getProductLogo,
            bundle = null
        )?.let { logo ->
            mProductLogo = logo
        }

        isLaunch = isLaunchMode()




        when {
            isLaunch -> {
                // 设置主题
                setTheme(R.style.Theme_LibEcosed)
                // 启用边倒边
                EdgeToEdgeUtils.applyEdgeToEdge(window, true)
                WindowCompat.setDecorFitsSystemWindows(window, false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


                TopAppBarUtils.newInstance().attach { visible, onTouchListener, function ->
                    isVisible = visible
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


                setContent {
//                        content.value?.invoke()
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
                            productLogo = mProductLogo,
                            topBarVisible = isVisible,
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
        content: ComponentActivity.() -> Unit,
    ): Unit = superUnit {
        when (mME) {
            is ComponentActivity -> {
                (mME as ComponentActivity).content()
            }

            else -> error(message = errorActCls)
        }
    }

    // 为了不将引擎暴露通过上下文包装器传递需要重定义为引擎
    private fun <T> engineUnit(
        content: EcosedEngine.() -> T,
    ): T? = (mAPP.engine as EcosedEngine).content()

    private fun <T> defaultUnit(
        content: ComponentActivity.() -> T,
    ): T = when (mME) {
        is ComponentActivity -> {
            (mME as ComponentActivity).content()
        }

        else -> error(message = errorActCls)
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

        const val errorActCls: String = "错误: Activity需要继承ComponentActivity或其子类!"

    }
}