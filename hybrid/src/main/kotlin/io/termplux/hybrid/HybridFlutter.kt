package io.termplux.hybrid

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.fragment.app.Fragment
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode

class HybridFlutter : ContextWrapper(null), HybridWrapper {

    private lateinit var mApplication: Application
    private lateinit var mFragment: FlutterBoostFragment

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun withApplication(application: Application): HybridWrapper {
        return this@HybridFlutter.apply {
            mApplication = application
        }
    }

    override fun build(): HybridFlutter {
        return this@HybridFlutter.apply {
            // 附加基础上下文
            attachBaseContext(
                base = mApplication.baseContext
            )
            // 初始化FlutterBoost
            FlutterBoost.instance().setup(
                mApplication,
                FlutterDelegate.build(),
                FlutterCallBack.build(
                    plugin = EnginePlugin.build()
                )
            )
            // 初始化FlutterFragment
            FlutterBoostFragment.CachedEngineFragmentBuilder()
                .destroyEngineWithFragment(false)
                .renderMode(RenderMode.surface)
                .transparencyMode(TransparencyMode.opaque)
                .shouldAttachEngineToActivity(false)
                .build<FlutterBoostFragment?>().let { flutter ->
                    mFragment = flutter
                }
        }
    }

    override fun getFlutterFragment(): Fragment {
        return mFragment
    }

    override fun onPostResume() {
        mFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        mFragment.onNewIntent(intent)
    }

    override fun onBackPressed() {
        mFragment.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        mFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        mFragment.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun onUserLeaveHint() {
        mFragment.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        mFragment.onTrimMemory(level)
    }

    companion object {

        fun build(): HybridFlutter {
            return HybridFlutter()
        }
    }
}