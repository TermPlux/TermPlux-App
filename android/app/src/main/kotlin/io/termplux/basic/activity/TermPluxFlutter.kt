package io.termplux.basic.activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.flutter.plugins.GeneratedPluginRegistrant
import io.termplux.R
import io.termplux.basic.custom.LinkNativeView
import io.termplux.basic.custom.LinkNativeViewFactory
import io.termplux.basic.fragment.AppsFragment

class TermPluxFlutter : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        //val registry = flutterEngine.platformViewsController.registry
        //   registry.registerViewFactory("android_view", LinkNativeViewFactory())
        flutterEngine.platformViewsController.registry.registerViewFactory(
            "android_view",
            object : PlatformViewFactory(
                StandardMessageCodec.INSTANCE
            ) {
                override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
                    return object : PlatformView {
                        override fun getView(): View {

                            return LinearLayoutCompat(context!!).apply {
                                orientation = LinearLayoutCompat.VERTICAL
                                background = ContextCompat.getDrawable(
                                    context,
                                    R.drawable.custom_wallpaper_24
                                )
                                addView(
                                    AppBarLayout(context).apply {
                                        addView(
                                            MaterialToolbar(context).apply {
                                                title = "6"
                                                subtitle = "6"
                                                logo = ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.baseline_terminal_24
                                                )
                                            },
                                            ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                            )
                                        )
                                    },
                                    ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                )
                            }
                        }

                        override fun dispose() {}
                    }
                }
            }
        )

        val messenger = flutterEngine.dartExecutor.binaryMessenger
        val channel = MethodChannel(messenger, "termplux_channel")
        channel.setMethodCallHandler { call, res ->
            when (call.method) {

                "" -> {}

                else -> {
                    res.error("error", "error_message", null)
                }
            }
        }
    }

    companion object {

    }
}