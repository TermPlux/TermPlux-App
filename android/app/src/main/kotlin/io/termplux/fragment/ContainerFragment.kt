package io.termplux.fragment

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.termplux.R
import io.termplux.activity.MainActivity

class ContainerFragment constructor(
    mainFragment: FlutterReturnFragment
) : BaseFragment<MainActivity>() {

    private lateinit var mFragmentManager: FragmentManager
    private var flutterFragment: FlutterReturnFragment? = null
    private val mMainFragment: FlutterReturnFragment

    init {
        mMainFragment = mainFragment
    }

    override fun resetContentView(): View {
        super.resetContentView()
        return FragmentContainerView(me).apply {
            id = R.id.flutter_container
        }
    }

    override fun initViews() {
        mFragmentManager = childFragmentManager
        flutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        ) as FlutterReturnFragment?
    }

    override fun initDatas() {
        if (flutterFragment == null) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterFragment = mMainFragment
                    add(
                        R.id.flutter_container,
                        mMainFragment,
                        tagFlutterFragment
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
                    flutterFragment = null
                }
            }
        )
    }

    companion object {
        private const val tagFlutterFragment = "flutter_boost_fragment"

        fun newInstance(
            mainFragment: FlutterReturnFragment
        ): ContainerFragment {
            return ContainerFragment(
                mainFragment = mainFragment
            )
        }
    }
}