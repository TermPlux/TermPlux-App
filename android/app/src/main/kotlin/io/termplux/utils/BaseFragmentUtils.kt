package io.termplux.utils

import android.view.View
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseFragment

class BaseFragmentUtils<Activity : BaseActivity> constructor(
    resetContentView: View,
    initView: () -> Unit,
    initData: () -> Unit,
    setEvent: () -> Unit
) : BaseFragment<Activity>() {

    private val mResetContentView: View
    private val mInitView: () -> Unit
    private val mInitData: () -> Unit
    private val mSetEvent: () -> Unit

    init {
        mResetContentView = resetContentView
        mInitView = initView
        mInitData = initData
        mSetEvent = setEvent
    }

    override fun resetContentView(): View {
        super.resetContentView()
        return mResetContentView
    }

    override fun initViews() = mInitView()

    override fun initDatas() = mInitData()

    override fun setEvents() = mSetEvent()

    companion object {

        fun <Activity : BaseActivity> newInstance(
            resetContentView: View,
            initView: () -> Unit,
            initData: () -> Unit,
            setEvent: () -> Unit
        ): BaseFragmentUtils<Activity> {
            return BaseFragmentUtils(
                resetContentView = resetContentView,
                initView = initView,
                initData = initData,
                setEvent = setEvent
            )
        }
    }
}