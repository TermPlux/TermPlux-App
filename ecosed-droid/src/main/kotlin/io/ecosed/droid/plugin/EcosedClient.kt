package io.ecosed.droid.plugin

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import io.ecosed.droid.app.EcosedApplicationImpl
import io.ecosed.droid.app.EcosedPlugin

/**
 * 作者: wyq0918dev
 * 仓库: https://github.com/ecosed/plugin
 * 时间: 2023/09/02
 * 描述: 客户端组件
 * 文档: https://github.com/ecosed/plugin/blob/master/README.md
 */
abstract class EcosedClient : ContextThemeWrapper(), DefaultLifecycleObserver, EcosedPlugin,
    PluginChannel.MethodCallHandler {

    /** 宿主FragmentActivity. */
    private lateinit var mActivity: FragmentActivity

    /** Application全局类. */
    private lateinit var mApplication: Application

    /** 宿主Fragment. */
    private lateinit var mFragment: Fragment

    /** 宿主Fragment的FragmentManager. */
    private lateinit var mFragmentManager: FragmentManager

    /** 宿主FragmentActivity的Window. */
    private lateinit var mWindow: Window

    /** 宿主FragmentActivity的WindowManager. */
    private lateinit var mWindowManager: WindowManager

    /** 插件通道 */
    private lateinit var mPluginChannel: PluginChannel

    /**
     * 附加基本上下文.
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    /**
     * 插件被添加时执行.
     */
    override fun onEcosedAdded(binding: PluginBinding) {
        mPluginChannel = PluginChannel(binding = binding, channel = channel)
        mPluginChannel.setMethodCallHandler(handler = this@EcosedClient)
    }

    /** 获取插件通信通道. */
    override val getPluginChannel: PluginChannel
        get() = mPluginChannel

    /**
     * 处理指定方法调用.
     */
    override fun onEcosedMethodCall(
        call: PluginChannel.MethodCall,
        result: PluginChannel.Result
    ) = Unit

    /**
     * 获取是否调试模式.
     * @return BuildConfig.DEBUG.
     */
    abstract fun isDebug(): Boolean

    /**
     * 获取LibEcosed框架 - LibEcosed框架专用接口.
     * @return mLibEcosed.
     */
    open fun getLibEcosed(): LibEcosed? {
        return null
    }

    /**
     * 产品图标 - LibEcosed框架专用接口.
     * @return ContextCompat.getDrawable(this, R.drawable.logo).
     */
    open fun getProductLogo(): Drawable? {
        return null
    }

    /**
     * 获取插件列表.
     * @return arrayListOf(ExamplePlugin()).
     */
    abstract fun getPluginList(): ArrayList<EcosedPlugin>?

    /**
     * 创建UI界面 - LibEcosed框架专用接口.
     * @param inflater LayoutInflater.
     * @param container ViewGroup.
     * @param savedInstanceState Bundle.
     * @return View?.
     */
    open fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }

    /**
     * UI完成创建时加载 - LibEcosed框架专用接口.
     * @param view View.
     * @param savedInstanceState Bundle.
     */
    open fun onViewCreated(view: View, savedInstanceState: Bundle?) = Unit

    /**
     * 获取Application全局类 - LibEcosed框架专用接口.
     * @return Application.
     */
    protected fun getApplication(): Application {
        return mApplication
    }

    /**
     * 获取宿主FragmentActivity的Window - LibEcosed框架专用接口.
     * @return 宿主FragmentActivity的Window.
     */
    protected fun getWindow(): Window {
        return mWindow
    }

    /**
     * 获取宿主FragmentActivity的WindowManager - LibEcosed框架专用接口.
     * @return 宿主FragmentActivity的WindowManager.
     */
    protected fun getWindowManager(): WindowManager {
        return mWindowManager
    }

    /**
     * 获取宿主Fragment - LibEcosed框架专用接口.
     * @return 宿主Fragment.
     */
    protected fun getFragment(): Fragment {
        return mFragment
    }

    /**
     * 获取宿主Fragment的childFragmentManager - LibEcosed框架专用接口.
     * @return 宿主Fragment的childFragmentManager.
     */
    protected fun getFragmentManager(): FragmentManager {
        return mFragmentManager
    }

    /**
     * 获取宿主FragmentActivity - LibEcosed框架专用接口.
     * @return 宿主FragmentActivity.
     */
    protected fun getActivity(): FragmentActivity {
        return mActivity
    }

    /**
     * 调用插件代码的方法.
     * @param name 要调用的插件的通道.
     * @param method 要调用的插件中的方法.
     * @param bundle 通过Bundle传递参数.
     * @return 返回方法执行后的返回值.
     */
    protected fun <T> execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?
    ): T? {
        if (getApplication() is EcosedApplicationImpl) {
            (getApplication() as EcosedApplicationImpl).apply {
                return getPluginEngine().execMethodCall<T>(
                    name = name,
                    method = method,
                    bundle = bundle
                )
            }
        } else error(
            message = "错误:EcosedApplication接口未实现.\n" +
                    "提示1:可能未在应用的Application全局类实现EcosedApplication接口.\n" +
                    "提示2:应用的Application全局类可能未在AndroidManifest.xml中注册."
        )
    }

    /**
     * 客户端组件第一次初始化 - 内部API.
     * @param base 通过引擎设置基础上下文.
     */
    internal fun firstAttach(base: Context) {
        this@EcosedClient.attachBaseContext(
            base = base
        )
    }

    /**
     * 客户端组件第二次初始化 - LibEcosed框架专用接口.
     * @param activity 在宿主Fragment中传入requireActivity().
     * @param application 在宿主Fragment中传入requireActivity().application.
     * @param fragment 在宿主Fragment中传入this.
     * @param fragmentManager 在宿主Fragment中传入childFragmentManager.
     * @param window 在宿主Fragment中传入requireActivity().window.
     * @param windowManager 在宿主Fragment中传入requireActivity().windowManager.
     */
    fun secondAttach(
        activity: FragmentActivity,
        application: Application,
        fragment: Fragment,
        fragmentManager: FragmentManager,
        window: Window,
        windowManager: WindowManager
    ) {
        mActivity = activity
        mApplication = application
        mFragment = fragment
        mFragmentManager = fragmentManager
        mWindow = window
        mWindowManager = windowManager
    }

    companion object {
        const val channel: String = ""
    }
}