package io.termplux.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.idlefish.flutterboost.containers.FlutterBoostFragment
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.termplux.R

class HomeFragment : Fragment() {

    private var mainFragment: MainFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentContainerView(requireActivity()).apply {
            id = R.id.flutter_container
        }.also {
            val fragmentManager = childFragmentManager
            mainFragment = fragmentManager.findFragmentByTag(
                tagFlutterBoostFragment
            ) as MainFragment?
            if (mainFragment == null) {
                val mFlutterBoostFragment = FlutterBoostFragment.CachedEngineFragmentBuilder(
                    MainFragment::class.java
                )
                    .url("home")
                    .destroyEngineWithFragment(false)
                    .renderMode(RenderMode.surface)
                    .transparencyMode(TransparencyMode.opaque)
                    .shouldAttachEngineToActivity(true)
                    .build<MainFragment>()
                fragmentManager.commit(
                    allowStateLoss = false,
                    body = {
                        mainFragment = mFlutterBoostFragment
                        add(
                            it.id,
                            mFlutterBoostFragment,
                            tagFlutterBoostFragment
                        )
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainFragment = null
    }

    companion object {
        const val tagFlutterBoostFragment: String = "flutter_boost_fragment"
    }
}