package io.termplux.fragment

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.flutter.embedding.android.FlutterFragment
import io.termplux.R
import io.termplux.activity.TermPluxActivity

class HomeFragment constructor(
    flutter: FlutterFragment
) : BaseFragment<TermPluxActivity>() {

    private val mFlutter: FlutterFragment
    private lateinit var fragmentManager: FragmentManager
    private var flutterFragment: FlutterFragment? = null

    init {
        mFlutter = flutter
    }

    override fun resetContentView(): View {
        super.resetContentView()
        return FragmentContainerView(me).apply {
            id = R.id.flutter_container
        }
    }

    override fun initViews() {
        fragmentManager = childFragmentManager
        flutterFragment = fragmentManager.findFragmentByTag(tagFlutterFragment) as FlutterFragment?
    }

    override fun initDatas() {
        if (flutterFragment == null) {
            fragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterFragment = mFlutter
                    add(
                        R.id.flutter_container,
                        mFlutter,
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
        private const val tagFlutterFragment = "flutter_fragment"

        fun newInstance(
            flutter: FlutterFragment
        ): HomeFragment {
            return HomeFragment(
                flutter = flutter
            )
        }
    }
}