package io.termplux.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.WallpaperManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.util.AppManager
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnBindView
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.GnuGeneralPublicLicense30
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import io.termplux.R
import io.termplux.ui.ActivityMain
import io.termplux.ui.view.ScrollControllerWebView
import io.termplux.values.Codes
import io.termplux.values.Packages
import kotlin.system.exitProcess


class MainActivityUtils(
    private val mContext: BaseActivity
) {

    private lateinit var webView: ScrollControllerWebView

    @SuppressLint("SetJavaScriptEnabled")
    fun initView() {
        webView = ScrollControllerWebView(mContext).apply {
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

    @Composable
    fun Content(
        gridView: GridView,
        appsList: List<ResolveInfo>,
        dynamicColorChecked: Boolean,
        taskBarChecked: Boolean
    ) {
        ActivityMain(
            androidVersion = getAndroidVersion(),
            shizukuVersion = getShizukuVersion(),
            gridView = gridView,
            appsList = appsList,
            isSystemApps = { packageName ->
                isSystemApplication(
                    packageName = packageName
                )
            },
            startApp = { packageName, className ->
                startApplication(
                    packageName = packageName,
                    className = className
                )
            },
            infoApp = { packageName ->
                infoApplication(
                    packageName = packageName
                )
            },
            deleteApp = { packageName ->
                deleteApplication(
                    packageName = packageName
                )
            },
            targetAppVersionName = "",

            dynamicColorChecked = dynamicColorChecked,
            taskBarChecked = taskBarChecked,
            onEasterEgg = {
                easterEgg()
            },
            onNotice = {
                licenseDialog()
            },
            onSource = {
                fullScreenWebView("https://github.com/TermPlux/TermPlux-App")
            },
            onDevGitHub = {
                fullScreenWebView("https://github.com/wyq0918dev")
            },
            onDevTwitter = {
                fullScreenWebView("https://twitter.com/wyq0918dev")
            },
            onTeamGitHub = {
                fullScreenWebView("https://github.com/TermPlux")
            },
        )
    }

    private fun fullScreenWebView(url: String) {
        FullScreenDialog.show(
            object : OnBindView<FullScreenDialog>(webView) {
                override fun onBind(dialog: FullScreenDialog?, v: View?) {
                    webView.loadUrl(url)
                }
            }
        )
    }

    private fun licenseDialog() {
        val notices = Notices()
        notices.addNotice(Notice("AndroidUtilCode", "", "Copyright (c) Blankj", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("LibTaskBar", "", "Copyright (c) farmerbb", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("BaseFramework", "", "Copyright BaseFramework", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("DialogX", "", "Copyright Kongzue DialogX", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("AndroidHiddenApiBypass", "", "Copyright 2021-2023 LSPosed", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("FakeStore", "", "Copyright (c) 2013-2016 microG Project Team", ApacheSoftwareLicense20()))
        notices.addNotice(Notice("Shizuku-API", "", "Copyright (c) RikkaApps", MITLicense()))
        notices.addNotice(Notice("Termux App", "", "Copyright (c) Termux", GnuGeneralPublicLicense30()))
        notices.addNotice(Notice("UserLAnd", "", "Copyright (c) CypherpunkArmory", ApacheSoftwareLicense20()))
        LicensesDialog.Builder(mContext)
            .setNotices(notices)
            .setIncludeOwnLicense(true)
            .build()
            .show()
    }

    private fun startApplication(
        packageName: String,
        className: String
    ) {
        try {
            // 启动目标应用
            val intent = Intent()
            intent.component = ComponentName(packageName, className)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        } catch (e: Exception) {
            // 如果已卸载但未刷新跳转应用市场防止程序崩溃
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun infoApplication(packageName: String) {
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.parse("package:$packageName")
            mContext.startActivity(intent)
        } catch (e: Exception) {
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun deleteApplication(packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_DELETE)
            intent.data = Uri.parse("package:$packageName")
            mContext.startActivity(intent)
        } catch (e: Exception) {
            openApplicationMarket(packageName = packageName)
        }
    }

    private fun openApplicationMarket(packageName: String) {
        val str = "market://details?id=$packageName"
        val localIntent = Intent(Intent.ACTION_VIEW)
        localIntent.data = Uri.parse(str)
        mContext.startActivity(localIntent)
    }

    /**
     * 检测应用是否为系统应用
     */
    private fun isSystemApplication(packageName: String): Boolean {
        try {
            val packageInfo = mContext.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_CONFIGURATIONS
            )
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    private fun easterEgg() {

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


    private fun getAndroidVersion(): String {
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

    private fun getShizukuVersion(): String {
        return "114514"
    }

    // 打开任务栏设置
    private fun taskbarSettings() {
        Taskbar.openSettings(
            mContext,
            mContext.getString(R.string.taskbar_title),
            R.style.Theme_TermPlux_ActionBar
        )
    }

    // 选择默认主屏幕应用
    private fun defaultLauncher() {
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