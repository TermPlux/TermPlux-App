package io.termplux.basic.fragment

import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.appbar.AppBarLayout
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.flutter.embedding.android.FlutterFragment
import io.termplux.R
import io.termplux.basic.activity.TermPluxActivity

class HomeFragment constructor(
    flutter: FlutterFragment,
    appBar: AppBarLayout
) : BaseFragment<TermPluxActivity>() {

    private val mFlutter: FlutterFragment
    private val mAppBar: AppBarLayout
    private lateinit var fragmentManager: FragmentManager
    private var flutterFragment: FlutterFragment? = null

    init {
        mFlutter = flutter
        mAppBar = appBar
    }

    override fun resetContentView(): View {
        super.resetContentView()
        return LinearLayoutCompat(me).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                mAppBar,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                FragmentContainerView(me).apply {
                    id = R.id.flutter_container
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
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
            flutter: FlutterFragment,
            appBar: AppBarLayout
        ): HomeFragment{
            return HomeFragment(
                flutter = flutter,
                appBar = appBar
            )
        }
    }
}