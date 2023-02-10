package io.termplux.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.WallpaperManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.provider.Settings
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.util.AppManager
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnBindView
import io.termplux.R
import io.termplux.ui.view.ScrollControllerWebView
import io.termplux.values.Codes
import io.termplux.values.Key
import io.termplux.values.Packages
import kotlin.system.exitProcess


class MainActivityUtils(
    private val mContext: Context
) {

    fun initViews(){

    }


    fun web() {
        val webView = ScrollControllerWebView(mContext)
        val close = AppCompatButton(mContext)
        FullScreenDialog.show(
            object : OnBindView<FullScreenDialog>(webView) {
                @SuppressLint("SetJavaScriptEnabled")
                override fun onBind(dialog: FullScreenDialog?, v: View?) {
                    val webSettings = webView.settings
                    webSettings.javaScriptEnabled = true
                    webSettings.loadWithOverviewMode = true
                    webSettings.useWideViewPort = true
                    webSettings.setSupportZoom(false)
                    webSettings.allowFileAccess = true
                    webSettings.javaScriptCanOpenWindowsAutomatically = true
                    webSettings.loadsImagesAutomatically = true
                    webSettings.defaultTextEncodingName = "utf-8"
                    webView.webViewClient = WebViewClient()
                    webView.loadUrl("https://github.com/TermPlux/TermPlux-App")
                }
            }
        )
    }

    // 检查设备是否支持谷歌基础服务
    fun checkGooglePlayServices(
        onNoGms: () -> Unit
    ) {
        val code =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext)
        if (code != ConnectionResult.SUCCESS) {
            if (!AppUtils.isAppInstalled(Packages.gms)) {
                GoogleApiAvailability.getInstance()
                    .makeGooglePlayServicesAvailable(mContext as AppCompatActivity)
                if (GoogleApiAvailability.getInstance().isUserResolvableError(code)) {
                    onNoGms()
                }
            }
        }
    }

    // 服务绑定的监听器
    val mConnection: ServiceConnection = object : ServiceConnection {
        // 后台服务绑定成功后执行
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            PopTip.show("服务绑定成功")
        }

        // 后台服务绑定失败后执行
        override fun onServiceDisconnected(arg0: ComponentName) {
            val msg = "error"
            try {
                PopTip.show(msg)
            } catch (e: Exception) {
                Toast.makeText(
                    AppManager.getInstance().activeActivity,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun getAndroidVersion(): String {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.N -> "Android Nougat 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat 7.1"
            Build.VERSION_CODES.O -> "Android Oreo 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo 8.1"
            Build.VERSION_CODES.P -> "Android Pie 9.0"
            Build.VERSION_CODES.Q -> "Android Queen Cake 10.0"
            Build.VERSION_CODES.R -> "Android Red Velvet Cake 11.0"
            Build.VERSION_CODES.S -> "Android Snow Cone 12.0"
            Build.VERSION_CODES.S_V2 -> "Android Snow Cone V2 12.1"
            Build.VERSION_CODES.TIRAMISU -> "Android Tiramisu 13.0"
            34 -> "Android Upside Down Cake 14.0"
            else -> "unknown"
        }
    }

    fun getShizukuVersion(): String {
        return "shit"
    }

    // 打开任务栏设置
    fun taskbarSettings() {
        Taskbar.openSettings(
            mContext,
            mContext.getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }

    // 选择默认主屏幕应用
    fun defaultLauncher() {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(intent)
    }


    fun killAppProcess(context: Context) {
        val mActivityManager = context.getSystemService(BaseApp.ACTIVITY_SERVICE) as ActivityManager
        val mList = mActivityManager.runningAppProcesses
        for (runningAppProcessInfo in mList) {
            if (runningAppProcessInfo.pid != Process.myPid()) {
                Process.killProcess(runningAppProcessInfo.pid)
            }
        }
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }


    /**
     * Drawable转Bitmap
     */

    @SuppressLint("MissingPermission")
    fun bitmapWallpaper(): Bitmap {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(mContext) //获取壁纸管理器实例
        val bitmap: Bitmap //声明将要创建的bitmap
        val width = wallpaperManager.drawable.intrinsicWidth //获取图片宽度
        val height = wallpaperManager.drawable.intrinsicHeight //获取图片高度
        val config = Bitmap.Config.RGB_565 //图片位深
        bitmap = Bitmap.createBitmap(width, height, config) //创建一个空的Bitmap
        val canvas = Canvas(bitmap) //在bitmap上创建一个画布
        wallpaperManager.drawable.setBounds(0, 0, width, height) //设置画布的范围
        wallpaperManager.drawable.draw(canvas) //将drawable绘制在canvas上
        return bitmap
    }

    companion object {
        private val code = Codes
        private val key = Key

        const val none: Int = code.modeNone
        const val shizuku: Int = code.modeShizuku

        const val licence: String = key.licence
        const val dynamicColor: String = key.dynamicColor
        const val libTaskbar: String = key.libTaskBar

        /** 开屏图标动画时长 */
        const val splashPart1AnimatorMillis = 600
        const val splashPart2AnimatorMillis = 800

        /** 操作栏是否应该在[autoHideDelayMillis]毫秒后自动隐藏。*/
        const val autoHide = true

        /** 如果设置了[autoHide]，则在用户交互后隐藏操作栏之前等待的毫秒数。*/
        const val autoHideDelayMillis = 3000

        /** 一些较老的设备需要在小部件更新和状态和导航栏更改之间有一个小的延迟。*/
        const val uiAnimatorDelay = 300
    }
}