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
import io.termplux.app.utils.AppCompatFlutter


class ContainerFragment : Fragment() {

    private lateinit var mFragmentManager: FragmentManager
    private var flutterFragment: FlutterBoostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentManager = childFragmentManager
        flutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        ) as FlutterBoostFragment?
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
        if (flutterFragment == null) addFragment(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flutterFragment = null
    }

    private fun addFragment(view: View) {
        if (
            !getFlutterFragment().isAdded && mFragmentManager.findFragmentByTag(
                tagFlutterFragment
            ) == null
        ) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterFragment = getFlutterFragment()
                    add(
                        view.id,
                        getFlutterFragment(),
                        tagFlutterFragment
                    )
                }
            )
        }
    }

    private fun getFlutterFragment(): FlutterBoostFragment {
        return if (activity is AppCompatFlutter) {
            (activity as AppCompatFlutter).getFlutterFragment()
        } else {
            FlutterBoostFragment.CachedEngineFragmentBuilder(
                ReturnFragment::class.java
            )
                .destroyEngineWithFragment(false)
                .renderMode(RenderMode.surface)
                .transparencyMode(TransparencyMode.opaque)
                .shouldAttachEngineToActivity(false)
                .build()
        }
    }

    companion object {
        const val tagFlutterFragment = "flutter_boost_fragment"
    }
}