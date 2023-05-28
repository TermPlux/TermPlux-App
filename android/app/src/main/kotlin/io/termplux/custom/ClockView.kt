package io.termplux.custom

import android.content.Context
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextClock
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.blankj.utilcode.util.PhoneUtils
import com.kongzue.baseframework.BaseApp
import io.termplux.utils.ChineseCaleUtils

class ClockView : LinearLayoutCompat {

    constructor(
        context: Context
    ) : super(
        context
    )

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context,
        attrs
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        orientation = VERTICAL

        addView(
            TextClock(context).apply {
                gravity = Gravity.CENTER
                textSize = 40F
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addView(
            TextClock(context).apply {
                gravity = Gravity.CENTER
                textSize = 15F
                format12Hour = "yyyy/MM/dd E"
                format24Hour = "yyyy/MM/dd E"
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addView(
            AppCompatTextView(context).apply {
                gravity = Gravity.CENTER
                textSize = 15F
                text = ChineseCaleUtils.getChineseCale()
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
        addView(
            AppCompatTextView(context).apply {
                gravity = Gravity.CENTER
                textSize = 15F
                text = PhoneUtils.getSimOperatorName()
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )
    }
}