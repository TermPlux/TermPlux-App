package io.termplux.fragment

import android.content.Context
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.termplux.activity.TermPluxActivity

class PagerFragment constructor(
    appBar: AppBarLayout,
    pager: ViewPager2
) : BaseFragment<TermPluxActivity>() {

    // 上下文
    private val mContext: Context
    // View控件
    private val mAppBarLayout: AppBarLayout
    private val mViewPager2: ViewPager2

    init {
        // 获取上下文
        mContext = me
        // 获取控件实例
        mAppBarLayout = appBar
        mViewPager2 = pager
    }

    /**
     * 设置页面布局
     */
    override fun resetContentView(): View {
        super.resetContentView()
        return LinearLayoutCompat(
            mContext
        ).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                mAppBarLayout,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                mViewPager2,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun initViews() = Unit

    override fun initDatas() = Unit

    override fun setEvents() {
        setLifeCircleListener(
            object : LifeCircleListener(){

                override fun onCreate() {
                    super.onCreate()
                }

                override fun onResume() {
                    super.onResume()
                }

                override fun onPause() {
                    super.onPause()
                }

                override fun onDestroy() {
                    super.onDestroy()
                }
            }
        )
    }

    companion object {

        fun newInstance(
            appBar: AppBarLayout,
            pager: ViewPager2
        ): PagerFragment{
            return PagerFragment(
                appBar = appBar,
                pager = pager
            )
        }
    }
}