/**
 * Copyright EcosedKit
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
package io.ecosed.embedding

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.ecosed.R
import io.ecosed.engine.EcosedEngine
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode

class EcosedActivity<YourApplication : IEcosedApplication, YourActivity : IEcosedActivity> :
    ContextWrapper(null), IEcosedActivity, LifecycleOwner {


    private lateinit var mActivity: FragmentActivity
    private lateinit var mApplication: Application
    private lateinit var mLifecycle: Lifecycle
    private lateinit var mYourActivity: YourActivity
    private lateinit var mYourApplication: YourApplication


    private lateinit var mFragmentManager: FragmentManager
    private var mFlutterFragment: FlutterBoostFragment? = null


    private var isDebug = false


    private lateinit var mFlutterContainerView: FragmentContainerView


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun IEcosedActivity.attachEcosed(
        activity: FragmentActivity,
        lifecycle: Lifecycle,
    ) {
        // 附加基本上下文
        attachBaseContext(base = activity.baseContext)
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
        // 初始化片段管理器
        mFragmentManager = mActivity.supportFragmentManager
        // 初始化Flutter片段
        mFlutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        ) as FlutterBoostFragment?
        // 初始化片段容器视图
        mFlutterContainerView = FragmentContainerView(
            context = this@EcosedActivity
        ).apply {
            id = R.id.flutter_container
        }
        // 获取是否调试模式
        execMethodCall<Boolean>(
            channel = io.ecosed.client.EcosedClient.mChannelName,
            method = io.ecosed.client.EcosedClient.mMethodDebug
        )?.let { debug ->
            isDebug = debug
        }
        // 如果传入错误的类则抛出异常
        when {
            mYourActivity !is FragmentActivity -> error(
                message = errorActivityExtends
            )

            mYourApplication !is Application -> error(
                message = errorApplicationExtends
            )
        }
    }


    override fun IEcosedActivity.setContentSpace(
        block: (
            flutter: View,
            commit: () -> Unit,
        ) -> Unit,
    ) = defaultUnit {
        block.invoke(mFlutterContainerView) {
            if (mFlutterFragment == null) {
                addFragment()
            }
        }
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

    override fun IEcosedActivity.detachEcosed() {
        mFlutterFragment = null
    }

    private fun addFragment() {
        mFlutterFragment = getFlutterFragment()
        if (mFlutterFragment?.isAdded == false and (mFragmentManager.findFragmentByTag(
                tagFlutterFragment
            ) == null)
        ) {
            mFlutterFragment?.let { flutter ->
                mFragmentManager
                    .beginTransaction()
                    .add(
                        mFlutterContainerView.id,
                        flutter,
                        tagFlutterFragment
                    )
                    .commit()
            }
        }
    }


    private fun getFlutterFragment(): FlutterBoostFragment {
        return FlutterBoostFragment.CachedEngineFragmentBuilder()
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(false)
            .build()
    }

    override val lifecycle: Lifecycle
        get() = mLifecycle

    private fun hasSuperUnit(
        superUnit: (() -> Unit) -> Unit,
        content: FragmentActivity.() -> Unit,
    ): Unit = superUnit {
        when (mYourActivity) {
            is FragmentActivity -> {
                (mYourActivity as FragmentActivity).content()
            }

            else -> error(
                message = errorActivityExtends
            )
        }
    }

    private fun <T> engineUnit(
        content: EcosedEngine.() -> T,
    ): T? = (mYourApplication.engine as EcosedEngine).content()

    private fun <T> defaultUnit(
        content: FragmentActivity.() -> T,
    ): T = when (mYourActivity) {
        is FragmentActivity -> {
            (mYourActivity as FragmentActivity).content()
        }

        else -> error(
            message = errorActivityExtends
        )
    }

    private companion object {
        const val tagFlutterFragment = "flutter_boost_fragment"


        const val errorActivityExtends: String =
            "错误: EcosedActivity只能在FragmentActivity或其子类中使用!"
        const val errorApplicationExtends: String =
            "错误: EcosedApplication只能在Application中使用!"
    }
}