package io.termplux.basic.custom

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.view.Gravity
import android.widget.TextClock
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.baseframework.BaseApp
import io.termplux.R
import io.termplux.basic.utils.ChineseCaleUtils

@SuppressLint("ViewConstructor")
class LauncherView(
    context: Context,
    recyclerView: RecyclerView
) : LinearLayoutCompat(
    context
) {

    /** 上下文 */
    private val mContext: Context

    /** 启动器列表 */
    private val mRecyclerView: RecyclerView

    private val mDayLunar: String
    private val mCarrierName: String

    init {
        mContext = context
        mRecyclerView = recyclerView
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

        fitsSystemWindows = true

        // 垂直布局
        orientation = VERTICAL

        // 设置背景
        background = ContextCompat.getDrawable(
            mContext,
            R.drawable.custom_wallpaper_24
        )
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

        // 农历
        addView(
            AppCompatTextView(
                mContext
            ).apply {
                textSize = 16f
                gravity = Gravity.CENTER
                text = mDayLunar
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // 运营商
        addView(
            AppCompatTextView(
                mContext
            ).apply {
                textSize = 16f
                gravity = Gravity.CENTER
                text = mCarrierName
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        // 启动器列表
        addView(
            mRecyclerView.apply {
                layoutManager = GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false)
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    companion object {

    }
}