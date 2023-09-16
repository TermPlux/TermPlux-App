package io.ecosed.droid.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.plugin.PluginExecutor

class EcosedActivityUtils<YourActivity : Activity> : ContextWrapper(null), EcosedActivityImpl,
    DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private var layoutResId = -1
    private var isDebug = false

    private lateinit var mME: YourActivity

    override fun Activity.attachUtils(activity: Activity, lifecycle: Lifecycle) {
        attachBaseContext(activity.baseContext)
        mActivity = activity
        @Suppress("UNCHECKED_CAST")
        mME = mActivity as YourActivity
        lifecycle.addObserver(observer = this@EcosedActivityUtils)
    }

    override fun Activity.detachUtils(lifecycle: Lifecycle) {
        lifecycle.removeObserver(observer = this@EcosedActivityUtils)
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

    override fun onCreate(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onCreate(owner = owner)
            content()
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

                if (isDebug) AlertDialog.Builder(mActivity).apply {

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
            super.onStart(owner = owner)
            content()
        }
    ) {

    }

    override fun onResume(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onResume(owner = owner)
            content()
        }
    ) {

    }

    override fun onPause(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onPause(owner = owner)
            content()
        }
    ) {

    }

    override fun onStop(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onStop(owner = owner)
            content()
        }
    ) {

    }

    override fun onDestroy(owner: LifecycleOwner) = lifecycleUnit(
        superUnit = { content ->
            super.onDestroy(owner = owner)
            content()
        }
    ) {

    }

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
            return@defaultUnit isLauncher != null
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