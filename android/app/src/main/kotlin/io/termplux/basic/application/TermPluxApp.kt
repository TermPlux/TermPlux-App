package io.termplux.basic.application

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import com.farmerbb.taskbar.lib.Taskbar
import com.google.android.material.color.DynamicColors
import com.kongzue.baseframework.BaseApp
import com.kongzue.baseframework.interfaces.OnBugReportListener
import com.kongzue.baseframework.util.AppManager
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogxmaterialyou.style.MaterialYouStyle
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.io.File


class TermPluxApp : BaseApp<TermPluxApp>() {

    /** 首选项 */
    private lateinit var mSharedPreferences: SharedPreferences

    /**
     * 应用启动时执行
     */
    override fun init() {

        // 加载首选项
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@TermPluxApp)

//        FlutterBoost.instance().setup(me,
//            object : FlutterBoostDelegate {
//                override fun pushNativeRoute(options: FlutterBoostRouteOptions) {
//                    //这里根据options.pageName来判断你想跳转哪个页面，这里简单给一个
//                    val intent = when (options.pageName()){
//                        "tpmain" -> Intent(
//                            FlutterBoost.instance().currentActivity(),
//                            MainActivity().javaClass
//                        )
//                        else -> Intent()
//                    }
//
//                    FlutterBoost.instance().currentActivity().startActivityForResult(intent, options.requestCode())
//                }
//
//                override fun pushFlutterRoute(options: FlutterBoostRouteOptions) {
//                    val intent = FlutterBoostActivity.CachedEngineIntentBuilder(
//                        MainFlutterActivity().javaClass
//                    )
//                        .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
//                        .destroyEngineWithActivity(false)
//                        .uniqueId(options.uniqueId())
//                        .url(options.pageName())
//                        .urlParams(options.arguments())
//                        .build(FlutterBoost.instance().currentActivity())
//                    FlutterBoost.instance().currentActivity().startActivity(intent)
//                }
//            }
//        ) { engine: FlutterEngine? ->
//            engine.apply {
//
//            }
//        }

        // 触发错误时调用
        setOnCrashListener(
            object : OnBugReportListener() {
                override fun onCrash(e: Exception, crashLogFile: File): Boolean {
                    if (AppManager.getInstance().activeActivity == null || !AppManager.getInstance().activeActivity.isActive) {
                        return false
                    }
                    try {
                        MessageDialog.show(
                            "Ops！发生了一次崩溃！",
                            "您是否愿意帮助我们改进程序以修复此Bug？"
                        )
                            .setOkButton("愿意") { dialog, v ->
                                toast("我真的会谢")
                                false
                            }
                            .setCancelButton("不了") { dialog, v ->
                                toast("抱歉打扰了")
                                false
                            }
                    } catch (e: Exception) {
                        log(Log.getStackTraceString(e))
                    }
                    return false
                }
            }
        )
    }

    /**
     * 去除隐藏API访问限制
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }


    /**
     * 加载SDK
     */
    override fun initSDKs() {
        super.initSDKs()

        // 初始化DialogX
        DialogX.init(this@TermPluxApp)
        DialogX.globalStyle = MaterialYouStyle()
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.autoShowInputKeyboard = true
        DialogX.onlyOnePopTip = false
        DialogX.cancelable = true
        DialogX.cancelableTipDialog = false
        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
        DialogX.autoRunOnUIThread = true
        DialogX.useHaptic = true

        // 初始化动态颜色
        if (mSharedPreferences.getBoolean(
                "dynamic_colors",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) DynamicColors.applyToActivitiesIfAvailable(this@TermPluxApp)


        // 初始化任务栏
        Taskbar.setEnabled(
            this@TermPluxApp,
            mSharedPreferences.getBoolean(
                "desktop",
                true
            ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )
    }

    /**
     * SDK加载完成时调用
     */
    override fun initSDKInitialized() {
        super.initSDKInitialized()
        val msg = "SDK已加载完毕"
        try {
            PopTip.show(msg)
        } catch (e: Exception) {
            toast(msg)
        }
    }
}