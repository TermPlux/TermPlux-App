package io.termplux.custom

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.Composable
import io.flutter.embedding.android.FlutterView

@SuppressLint("ViewConstructor")
class RootLayout constructor(
    context: Context,
    flutter: FlutterView,
    splash: AppCompatImageView,
    fullMode: Boolean
) : FrameLayout(
    context
) {

    private val mContext: Context
    private val mFlutterView: FlutterView
    private val mSplashLogo: AppCompatImageView
    private val mFullMode: Boolean

    init {
        mContext = context
        mFlutterView = flutter
        mSplashLogo = splash
        mFullMode = fullMode
        addView(
            mFlutterView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
        addView(
            mSplashLogo,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
    }

    fun setContent(content: @Composable (root: FrameLayout) -> Unit) {
        this@RootLayout.also { view ->
            when (mFullMode) {
                true -> if (mContext is AppCompatActivity) mContext.setContent {
                    content(root = view)
                }
                false -> if (mContext is AppCompatActivity) mContext.setContentView(view)
            }
        }
    }
}