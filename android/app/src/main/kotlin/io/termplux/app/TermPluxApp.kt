package io.termplux.app

import com.blankj.utilcode.util.AppUtils
import com.google.android.material.color.DynamicColors
import com.tencent.bugly.crashreport.CrashReport
import io.flutter.app.FlutterApplication

class TermPluxApp : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
        // 启用动态取色
        DynamicColors.applyToActivitiesIfAvailable(
            this@TermPluxApp
        )
        // 初始化Bugly
        CrashReport.initCrashReport(
            this@TermPluxApp,
            buglyAppId,
            AppUtils.isAppDebug()
        )
    }

    companion object {
        const val buglyAppId: String = "c10f20dd8d"
    }
}