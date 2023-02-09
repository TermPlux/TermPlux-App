package io.termplux.core

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.termplux.services.MainService
import io.termplux.values.Packages
import rikka.shizuku.Shizuku

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

    fun stop(context: Context) {
        val intent = Intent(context, MainService().javaClass)
        context.stopService(intent)
    }


}