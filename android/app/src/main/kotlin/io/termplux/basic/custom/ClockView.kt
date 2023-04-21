package io.termplux.basic.custom

import android.content.Context
import android.telephony.TelephonyManager
import android.view.Gravity
import android.widget.TextClock
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.kongzue.baseframework.BaseApp
import io.termplux.basic.utils.ChineseCaleUtils

class ClockView(context: Context) : LinearLayoutCompat(context) {

    private val mContext: Context

    private val mDayLunar: String
    private val mCarrierName: String

    init {
        mContext = context

        mDayLunar = getDayLunar()
        mCarrierName = getCarrierName()

        addParams()
        addView()
    }

    /**
     * 获取农历
     */
    private fun getDayLunar(): String {
        return ChineseCaleUtils.getChineseCale()
    }

    /**
     * 获取运营商
     */
    private fun getCarrierName(): String {
        val telephony: TelephonyManager = mContext.getSystemService(
            BaseApp.TELEPHONY_SERVICE
        ) as TelephonyManager
        return telephony.simOperatorName
    }

    private fun addParams(){
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        // 垂直布局
        orientation = VERTICAL
    }

    private fun addView(){
        // 时间
        addView(
            TextClock(
                mContext
            ).apply {
                format12Hour = "hh:mm"
                format24Hour = "HH:mm"
                textSize = 40f
                gravity = Gravity.CENTER
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // 日期
        addView(
            TextClock(
                mContext
            ).apply {
                format12Hour = "yyyy/MM/dd E"
                format24Hour = "yyyy/MM/dd E"
                textSize = 16f
                gravity = Gravity.CENTER
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

//        // 农历
//        addView(
//            AppCompatTextView(
//                mContext
//            ).apply {
//                textSize = 16f
//                gravity = Gravity.CENTER
//                text = mDayLunar
//            },
//            LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT
//            )
//        )
//
//        // 运营商
//        addView(
//            AppCompatTextView(
//                mContext
//            ).apply {
//                textSize = 16f
//                gravity = Gravity.CENTER
//                text = mCarrierName
//            },
//            LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT
//            )
//        )
    }
}