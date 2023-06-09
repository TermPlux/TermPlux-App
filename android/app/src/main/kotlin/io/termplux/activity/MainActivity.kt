package io.termplux.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.*
import android.graphics.Color
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.internal.EdgeToEdgeUtils
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.util.JumpParameter
import com.kongzue.dialogx.dialogs.PopTip
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.R
import io.termplux.custom.LinkNativeViewFactory
import io.termplux.delegate.BoostDelegate
import io.termplux.fragment.MainFragment
import io.termplux.plugin.TermPlux
import io.termplux.utils.LifeCircleUtils
import java.lang.ref.WeakReference

class MainActivity : BaseActivity(), FlutterPlugin, MethodCallHandler, TermPlux,
    DefaultLifecycleObserver, Runnable {

    private val mME: BaseActivity = me
    private val mContext: Context = mME

    private var mainFragment: MainFragment? = null

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var newMainFragment: MainFragment

    private lateinit var mFlutterView: FlutterView

    override fun resetContentView(): View {
        super.resetContentView()
        return FragmentContainerView(mContext).apply {
            id = R.id.flutter_container
        }
    }

    @SuppressLint("RestrictedApi")
    override fun initViews() {
        WeakReference(application).get()?.let { application ->
            // 初始化FlutterBoost
            FlutterBoost.instance().setup(
                application,
                BoostDelegate(plugin = this@MainActivity)
            ) { engine: FlutterEngine? ->
                engine?.plugins?.add(this@MainActivity)?.also {
                    GeneratedPluginRegistrant.registerWith(engine)
                }.also {
                    FlutterBoostFragment.CachedEngineFragmentBuilder(
                        MainFragment::class.java
                    ).destroyEngineWithFragment(false)
                        .renderMode(RenderMode.surface)
                        .transparencyMode(TransparencyMode.opaque)
                        .shouldAttachEngineToActivity(true)
                        .build<MainFragment>()?.also {
                            newMainFragment = it
                        }
                }


                // 引擎操作
                engine?.let {
                    val registry = it.platformViewsController.registry
                    registry.registerViewFactory("android_view", LinkNativeViewFactory())
                }
            }
        }





        lifecycle.addObserver(this@MainActivity)


        // 片段管理器
        mFragmentManager = supportFragmentManager
        // 初始化片段
        mainFragment = mFragmentManager.findFragmentByTag(
            tagFlutterBoostFragment
        ) as MainFragment?
        // 启用边到边
        EdgeToEdgeUtils.applyEdgeToEdge(window, true)
        // 深色模式跟随系统
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        // 设置页面布局边界
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setDarkStatusBarTheme(true)
        setDarkNavigationBarTheme(true)
        setNavigationBarBackgroundColor(Color.TRANSPARENT)
    }

    override fun initDatas(parameter: JumpParameter?) {

        // 显示Fragment
        if (mainFragment == null) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    mainFragment = newMainFragment
                    add(
                        R.id.flutter_container,
                        newMainFragment,
                        tagFlutterBoostFragment
                    )
                }
            )
        }
    }

    override fun setEvents() {
        // 生命周期监听
        setLifeCircleListener(
            LifeCircleUtils(
                baseActivity = mME
            ) {
                mainFragment = null
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> onBack()
            R.id.action_settings -> {
                PopTip.show("666")
            }
        }
        return true
    }

    override fun onPostResume() {
        super.onPostResume()
        newMainFragment.onPostResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        newMainFragment.onNewIntent(intent)
    }

    override fun onBack(): Boolean {
        super.onBack()
        newMainFragment.onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        newMainFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        newMainFragment.onUserLeaveHint()
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        newMainFragment.onTrimMemory(level)
    }

    companion object {
        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"
    }

    override fun onAttachedToEngine(binding: FlutterPluginBinding) {

    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {

    }

    override fun onMethodCall(call: MethodCall, result: Result) {

    }

    override fun init(application: Application) {

    }

    override fun pushNativeRoute(options: FlutterBoostRouteOptions) {

    }

    override fun configure(flutterEngine: FlutterEngine) {

    }

    override fun inflateFlutterView(flutterView: FlutterView?) {
        mFlutterView = flutterView ?: errorFlutterViewIsNull()
        (mFlutterView.parent as ViewGroup).removeView(mFlutterView)
        setContentView(mFlutterView)
    }

//    override fun attach(view: View?, flutterFragment: FlutterFragment): ComposeView {
//        TODO("Not yet implemented")
//    }

    override fun clean(flutterEngine: FlutterEngine) {

    }

    override fun run() {

    }

    override fun onCreate(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onDestroy(owner)
    }


    private fun errorFlutterViewIsNull(): Nothing {
        error("操你妈的这逼FlutterView是空的啊啊啊啊啊")
    }


}