package io.ecosed.droid.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.ecosed.droid.plugin.PluginExecutor

class EcosedActivityUtils<YourActivity : Activity> : ContextWrapper(null), EcosedActivityImpl,
    DefaultLifecycleObserver {

    private lateinit var mActivity: Activity
    private var layoutResId = -1

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


    private fun setLayout() {
        @SuppressLint("DiscouragedApi")
        layoutResId = resources.getIdentifier(guessNameOfLayoutResId(), "layout", packageName)
        if (layoutResId != 0) {
            mActivity.setContentView(layoutResId)
        } else {
            return
        }
    }

    private fun isLaunchMode(): Boolean {
        try {
            val iss = mActivity.javaClass.getAnnotation(EcosedDroidLauncher::class.java)
            return iss != null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

//    @Composable
//    override fun Content() {
//
//    }

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

    private fun isDefaultHome(): Boolean {
        var isHome = false
        mActivity.intent.categories.forEach {
            if (it == Intent.CATEGORY_HOME){
                isHome = true
            }
        }
        return isHome
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        setLayout()

        if (isLaunchMode()) {


        }


//        if (mActivity is ComponentActivity) {
//            (mActivity as ComponentActivity).apply {
//                if (mME is EcosedActivityImpl) {
//                    (mME as EcosedActivityImpl).apply {
//                        setContent {
//                            //Content()
//                        }
//                    }
//                }
//            }
//        }


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
        val words =
            mActivity.javaClass.simpleName.split("(?<!^)(?=[A-Z])".toRegex()).dropLastWhile {
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