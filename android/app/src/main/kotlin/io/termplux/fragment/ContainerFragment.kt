package io.termplux.fragment

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import com.kongzue.baseframework.BaseFragment
import com.kongzue.baseframework.interfaces.LifeCircleListener
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.termplux.R
import io.termplux.activity.TermPluxActivity

class ContainerFragment : BaseFragment<TermPluxActivity>() {

    private var mainFragment: MainFragment? = null
    private lateinit var mFragmentManager: FragmentManager

    override fun resetContentView(): View {
        super.resetContentView()
        return FragmentContainerView(me).apply {
            id = R.id.flutter_container
        }
    }

    override fun initViews() {
        mFragmentManager = childFragmentManager
        mainFragment = mFragmentManager.findFragmentByTag(
            TermPluxActivity.tagFlutterBoostFragment
        ) as MainFragment?
    }

    override fun initDatas() {
        // 初始化FlutterBoostFragment
        val newMainFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
            MainFragment::class.java
        )
            .destroyEngineWithFragment(false)
            .renderMode(RenderMode.surface)
            .transparencyMode(TransparencyMode.opaque)
            .shouldAttachEngineToActivity(true)
            .build<MainFragment>()

        // 显示Fragment
        if (mainFragment == null) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    mainFragment = newMainFragment
                    add(
                        R.id.flutter_container,
                        newMainFragment,
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
                    mainFragment = null
                }
            }
        )
    }

    companion object{

        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"

        fun newInstance(): ContainerFragment {
            return ContainerFragment()
        }
    }
}