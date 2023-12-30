package io.termplux.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class TPBaseFragment : Fragment(), TPBaseFragmentWrapper {

    private lateinit var mFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (requireActivity() is TPBaseActivityWrapper) {
            (requireActivity() as TPBaseActivityWrapper).apply {
                mFragment = hybrid.getFlutterFragment()
            }
        }
    }

    override val fragment: Fragment
        get() = mFragment
}