package io.termplux.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import com.kongzue.baseframework.BaseActivity
import io.termplux.ui.view.ScrollControllerWebView

class MainActivityUtils() {

    /** 上下文 */
    private lateinit var mContext: Context

    private lateinit var mSCWebView: ScrollControllerWebView

    constructor(context: BaseActivity) : this() {
        mContext = context
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView(){
        mSCWebView = ScrollControllerWebView(
            mContext
        ).apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(false)
            settings.allowFileAccess = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.loadsImagesAutomatically = true
            settings.defaultTextEncodingName = "utf-8"
        }
    }





    private external fun fuck()

    companion object {

        init {
            System.loadLibrary("termplux")
        }

        fun newInstance(): (BaseActivity) -> MainActivityUtils{
            return { context ->
                MainActivityUtils(context)
            }
        }

        fun initView(): () -> Unit {
            return {
                MainActivityUtils().initView()
            }
        }

        fun content() : @Composable () -> Unit {
            return {

            }
        }

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800

        const val none: Int = 0
        const val shizuku: Int = 1

        const val licence: String = "licence"
        const val dynamicColor: String = "dynamic_colors"
        const val libTaskBar: String = "lib_task_bar"
    }
}