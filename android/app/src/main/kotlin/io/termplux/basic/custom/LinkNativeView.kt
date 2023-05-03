package io.termplux.basic.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import io.flutter.plugin.platform.PlatformView

class LinkNativeView constructor(
    context: Context
) : PlatformView {

    private var mContext: Context

    init {
        mContext = context
    }

    override fun getView(): View {
        return LinearLayoutCompat(mContext).apply {
            orientation = LinearLayoutCompat.VERTICAL

            addView(
                MaterialButton(mContext).apply {
                    text = "666"

                },
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    override fun dispose() {

    }
}