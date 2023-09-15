package io.ecosed.droid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.ecosed.droid.plugin.PluginExecutor

class EcosedActivityUtils : ContextWrapper(null), EcosedActivityImpl,
    DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private var layoutResId = -1

    override fun Activity.attachUtils(activity: Activity, lifecycle: Lifecycle) {
        attachBaseContext(activity.baseContext)
        mActivity = activity
        lifecycle.addObserver(observer = this@EcosedActivityUtils)
    }

    override fun Activity.detachUtils(lifecycle: Lifecycle) {
        lifecycle.removeObserver(observer = this@EcosedActivityUtils)
    }


    override fun Activity.setLayout() {
        @SuppressLint("DiscouragedApi")
        layoutResId = resources.getIdentifier(guessNameOfLayoutResId(), "layout", packageName)
        Toast.makeText(this, guessNameOfLayoutResId(), Toast.LENGTH_SHORT).show()
        if (layoutResId != 0) {
            mActivity.setContentView(layoutResId)
        } else {
            return
        }
    }

    override fun <T> Activity.execMethodCall(name: String, method: String, bundle: Bundle?): T? {
        if (mActivity.application is EcosedAppImpl) {
            (mActivity.application as EcosedAppImpl).apply {
                return engine.execMethodCall<T>(
                    name = name,
                    method = method,
                    bundle = bundle
                )
            }
        } else error(
            message = ""
        )
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    private fun guessNameOfLayoutResId(): String {
        val words = mActivity.javaClass.simpleName.split("(?<!^)(?=[A-Z])".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()
        val stringBuffer = StringBuffer(words.size)
        for (i in words.indices.reversed()) {
            stringBuffer.append(words[i].lowercase())
            if (i != 0) stringBuffer.append("_")
        }
        return stringBuffer.toString()
    }
}