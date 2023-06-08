package io.termplux.plugin

import android.app.Application
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.idlefish.flutterboost.FlutterBoostRouteOptions
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine

interface TermPlux {

    /**
     * 程序初始化入口
     */
    fun init(application: Application)

    fun pushNativeRoute(options: FlutterBoostRouteOptions)

    /**
     * Flutter配置
     */
    fun configure(flutterEngine: FlutterEngine)

    /**
     * 程序加载并返回View
     */
    fun attach(view: View?, flutterFragment: FlutterFragment): ComposeView

    /**
     * Flutter清理
     */
    fun clean(flutterEngine: FlutterEngine)

}