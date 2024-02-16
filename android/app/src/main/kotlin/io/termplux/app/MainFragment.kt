package io.termplux.app

import androidx.fragment.app.Fragment
import com.wyq0918dev.container_fragment.ContainerFragment

class MainFragment : ContainerFragment() {

    override fun onCreateFragment(): Fragment {
        return (requireActivity() as ActivityWrapper).getFragment()
    }
}