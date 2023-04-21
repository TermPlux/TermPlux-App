package io.termplux.basic.custom

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import io.flutter.plugin.platform.PlatformView

class LinkNativeView constructor(
    context: Context
) : PlatformView {

    private var mContext: Context
    private lateinit var button: AppCompatButton
//    private var mWebView: WebView

    init {
        mContext = context

//        mWebView = WebView(mContext)


    }

    override fun getView(): AppCompatButton {

        button = AppCompatButton(mContext)

        button.text = "fuck"
        button.setOnClickListener {
            Toast.makeText(mContext, "oh ye", Toast.LENGTH_SHORT).show()
        }
        return button
    }

    override fun dispose() {

    }
}