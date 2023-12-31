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

class MainFragment : TPBaseFragment() {

    private lateinit var mFragmentManager: FragmentManager
    private var mFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentManager = childFragmentManager
        mFragment = mFragmentManager.findFragmentByTag(
            tagMainFragment
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentContainerView(
        context = requireContext()
    ).apply {
        id = R.id.flutter_container
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mFragment == null) {
            if (
                !fragment.isAdded && mFragmentManager.findFragmentByTag(
                    tagMainFragment
                ) == null
            ) {
                mFragmentManager.commit(
                    allowStateLoss = false,
                    body = {
                        mFragment = fragment
                        add(
                            view.id,
                            fragment,
                            tagMainFragment
                        )
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFragment = null
    }

    companion object {
        const val tagMainFragment = "main_fragment"
    }
}