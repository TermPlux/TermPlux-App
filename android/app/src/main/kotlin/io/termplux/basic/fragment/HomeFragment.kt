package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import io.flutter.embedding.android.FlutterFragment
import io.termplux.R

class HomeFragment constructor(
    flutterFragment: FlutterFragment
) : Fragment() {

    private val mFlutter: FlutterFragment

    private var flutterFragment: FlutterFragment? = null


    init {
        mFlutter = flutterFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager: FragmentManager = childFragmentManager
        flutterFragment = fragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentContainerView(
            requireActivity()
        ).apply {
            id = R.id.flutter_container
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (flutterFragment == null) {
            flutterFragment = mFlutter
            childFragmentManager.beginTransaction()
                .add(
                    R.id.flutter_container,
                    mFlutter,
                    TAG_FLUTTER_FRAGMENT
                )
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flutterFragment = null
    }

    companion object {

        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"

        fun newInstance(flutterFragment: FlutterFragment): HomeFragment{
            return HomeFragment(flutterFragment = flutterFragment)
        }
    }
}