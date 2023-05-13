package io.termplux.utils

import android.view.View
import com.kongzue.baseframework.BaseActivity
import com.kongzue.baseframework.BaseFragment

class BaseFragmentUtils<Activity : BaseActivity>(
    resetContentView: View,
    initView: (() -> Unit)? = null,
    initData: (() -> Unit)? = null,
    setEvent: (() -> Unit)? = null
) : BaseFragment<Activity>() {

    private val mResetContentView: View
    private val mInitView: (() -> Unit)?
    private val mInitData: (() -> Unit)?
    private val mSetEvent: (() -> Unit)?

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

    override fun initViews() = mInitView?.let { view ->
        view()
    } ?: Unit

    override fun initDatas() = mInitData?.let { data ->
        data()
    } ?: Unit

    override fun setEvents() = mSetEvent?.let { event ->
        event()
    } ?: Unit

    companion object {

        fun <Activity : BaseActivity> newInstance(
            resetContentView: View,
            initView: (() -> Unit)? = null,
            initData: (() -> Unit)? = null,
            setEvent: (() -> Unit)? = null
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