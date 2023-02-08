package io.termplux.core

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.util.AppManager
import com.kongzue.dialogx.dialogs.PopTip
import io.termplux.R
import io.termplux.activity.MainActivity
import io.termplux.services.BillingService
import io.termplux.values.Packages
import rikka.shizuku.Shizuku
import kotlin.system.exitProcess

/**
 * 此类“Core”是程序的核心部分，大部分功能的调用都要经过这里。
 * 此文件都是自定义的负责调用软件功能的方法。
 * 在未读懂源代码前不建议随意修改，容易出现各种各样的问题。
 * Design by wyq0918dev in 2023
 */

class Core {

    // 检查“读取外部存储”权限
    fun checkReadExternalStoragePermission(activity: AppCompatActivity): Boolean {
        return activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查读取内部存储/读取照片权限（用于获取桌面壁纸）
     */
    fun checkReadMedia(activity: AppCompatActivity): Boolean {
        return ContextCompat.checkSelfPermission(activity,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                android.Manifest.permission.READ_MEDIA_IMAGES
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 检查“Shizuku”权限
    fun checkShizukuPermission(): Boolean {
        return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    }

    // 检查设备是否支持谷歌基础服务
    fun checkGooglePlayServices(
        context: Context,
        onNoGms: () -> Unit
    ) {
        val code =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        if (code != ConnectionResult.SUCCESS) {
            if (!AppUtils.isAppInstalled(Packages.gms)) {
                GoogleApiAvailability.getInstance()
                    .makeGooglePlayServicesAvailable((context as AppCompatActivity))
                if (GoogleApiAvailability.getInstance().isUserResolvableError(code)) {
                    onNoGms()
                }
            }
        }
    }

    // 检查“签名欺骗”权限
    fun checkFakeSignaturePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context,"android.permission.FAKE_PACKAGE_SIGNATURE") == PackageManager.PERMISSION_GRANTED
    }

    // 获取“读取外部存储”权限
    fun grantReadExternalStoragePermission(activity: AppCompatActivity, code: Int) {
        activity.requestPermissions(
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            code
        )
    }

    fun grantReadMedia(activity: AppCompatActivity, code: Int) {
        activity.requestPermissions(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            },
            code
        )
    }

    // 获取“Shizuku”权限
    fun grantShizukuPermission(code: Int) {
        Shizuku.requestPermission(code)
    }

    // 获取“签名欺骗”权限
    fun grantFakeSignaturePermission(activity: AppCompatActivity, code: Int) {
        activity.requestPermissions(
            arrayOf("android.permission.FAKE_PACKAGE_SIGNATURE"),
            code
        )
    }


    // 服务绑定的监听器
    private val mConnection: ServiceConnection = object : ServiceConnection {
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

    // 启动后台服务和主页
    fun launch(context: Context) {
        val intent = Intent(context, MainActivity().javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    // 绑定后台服务
    fun service(context: Context) {
        val intent = Intent(context, BillingService().javaClass)
        context.startService(intent)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    fun stop(context: Context) {
        val intent = Intent(context, BillingService().javaClass)
        context.stopService(intent)
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



    // 获取系统代号
    fun getAndroidCodeName(): String {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.M -> "Marshmallow"
            Build.VERSION_CODES.N -> "Nougat"
            Build.VERSION_CODES.N_MR1 -> "Nougat MR1"
            Build.VERSION_CODES.O -> "Oreo"
            Build.VERSION_CODES.O_MR1 -> "Oreo MR1"
            Build.VERSION_CODES.P -> "Pie"
            Build.VERSION_CODES.Q -> "Q"
            Build.VERSION_CODES.R -> "RedVelvetCake"
            Build.VERSION_CODES.S -> "SnowCone"
            Build.VERSION_CODES.S_V2 -> "SnowCone V2"
            Build.VERSION_CODES.TIRAMISU -> "Tiramisu"
            34 -> "14"
            else -> "Unknown"
        }
    }

    // 获取系统版本
    fun getAndroidRelease(): String {
        return Build.VERSION.RELEASE
    }

    // 打开任务栏设置
    fun taskbarSettings(context: Context) {
        Taskbar.openSettings(context, "桌面模式设置", R.style.Theme_TermPlux_ActionBar)
    }

    // 选择默认主屏幕应用
    fun defaultLauncher(context: Context) {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

//    // 打开桌面设置
//    fun launcherSettings(context: Context) {
//        val intent = Intent(context, FixedSettings().javaClass)
//        context.startActivity(intent)
//    }
//
//    // 启动系统桌面
//    fun startLauncher(context: Context) {
//        val intent = Intent(context, FixedPlay().javaClass)
//        context.startActivity(intent)
//    }


}