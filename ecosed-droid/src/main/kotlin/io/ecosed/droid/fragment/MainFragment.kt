package io.ecosed.droid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.plugin.EcosedClient
import io.ecosed.droid.plugin.PluginExecutor

class MainFragment private constructor() : Fragment() {

    private lateinit var mClient: EcosedClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClient = PluginExecutor.execMethodCall<EcosedClient>(
            fragment = this@MainFragment,
            name = LibEcosedPlugin.channel,
            method = LibEcosedPlugin.getClient,
            bundle = null
        )!!
        mClient.secondAttach(
            activity = requireActivity(),
            application = requireActivity().application,
            fragment = this@MainFragment,
            fragmentManager = childFragmentManager,
            window = requireActivity().window,
            windowManager = requireActivity().windowManager
        )
        lifecycle.addObserver(mClient)




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return mClient.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClient.onViewCreated(view, savedInstanceState)
    }

    companion object {

        fun newInstance(): MainFragment{
            return MainFragment()
        }
    }
}