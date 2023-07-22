package io.termplux.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.termplux.app.R

class ContainerFragment : Fragment() {

    private lateinit var mFragmentManager: FragmentManager
    private var flutterFragment: ReturnFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentManager = childFragmentManager
        flutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        ) as ReturnFragment?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContainerView(
        context = requireContext()
    ).apply {
        id = R.id.flutter_container
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (flutterFragment == null) {
            val newFlutterFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
                ReturnFragment::class.java
            )
                .destroyEngineWithFragment(false)
                .renderMode(RenderMode.surface)
                .transparencyMode(TransparencyMode.opaque)
                .shouldAttachEngineToActivity(true)
                .build<ReturnFragment>()
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterFragment = newFlutterFragment
                    add(
                        view.id,
                        newFlutterFragment,
                        tagFlutterFragment
                    )
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flutterFragment = null
    }

    companion object {
        const val tagFlutterFragment = "flutter_boost_fragment"
    }
}