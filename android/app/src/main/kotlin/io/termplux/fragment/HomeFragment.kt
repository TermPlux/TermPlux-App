package io.termplux.fragment

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.termplux.R
import io.termplux.activity.TermPluxActivity

class HomeFragment constructor(
    flutter: FlutterBoostFragment
) : BaseFragment<TermPluxActivity>() {

    private val mFlutter: FlutterBoostFragment

    private lateinit var mFragmentManager: FragmentManager
    private var flutterBoostFragment: FlutterBoostFragment? = null

    init {
        mFlutter = flutter
    }

    override fun resetContentView(): View {
        super.resetContentView()
        return FragmentContainerView(
            me
        ).apply {
            id = R.id.flutter_container
        }
    }

    override fun initViews() {
        mFragmentManager = childFragmentManager
        flutterBoostFragment = mFragmentManager.findFragmentByTag(
            tagFlutterBoostFragment
        ) as FlutterBoostFragment?
    }

    override fun initDatas() {
        if (flutterBoostFragment == null) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterBoostFragment = mFlutter
                    add(
                        R.id.flutter_container,
                        mFlutter,
                        tagFlutterBoostFragment
                    )
                }
            )
        }
    }

    override fun setEvents() {
        setLifeCircleListener(
            object : LifeCircleListener() {
                override fun onDestroy() {
                    super.onDestroy()
                    flutterBoostFragment = null
                }
            }
        )
    }

    companion object {

        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"

        fun newInstance(flutter: FlutterBoostFragment): HomeFragment {
            return HomeFragment(flutter = flutter)
        }
    }
}