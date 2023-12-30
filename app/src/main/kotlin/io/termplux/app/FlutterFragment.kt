package io.termplux.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import io.termplux.base.TPBaseFragment

class FlutterFragment : TPBaseFragment() {

    private lateinit var mFragmentManager: FragmentManager
    private var flutterFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentManager = childFragmentManager
        flutterFragment = mFragmentManager.findFragmentByTag(
            tagFlutterFragment
        )
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
            !fragment.isAdded && mFragmentManager.findFragmentByTag(
                tagFlutterFragment
            ) == null
        ) {
            mFragmentManager.commit(
                allowStateLoss = false,
                body = {
                    flutterFragment = fragment
                    add(
                        view.id,
                        fragment,
                        tagFlutterFragment
                    )
                }
            )
        }
    }

    companion object {
        const val tagFlutterFragment = "flutter_fragment"
    }
}