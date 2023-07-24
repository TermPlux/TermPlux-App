package io.termplux.app.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class FlutterFactory : FrameLayout {

    constructor(
        context: Context
    ) : super(
        context
    )

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(
        context,
        attrs
    )

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun addView(child: View?) {
        super.addView(child)
    }
}