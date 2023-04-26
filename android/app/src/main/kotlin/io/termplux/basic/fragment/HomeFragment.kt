package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.flutter.embedding.android.FlutterFragment
import io.termplux.basic.custom.FragmentScaffold
import io.termplux.databinding.FragmentHomeBinding

class HomeFragment constructor(
    flutterFragment: FlutterFragment
) : Fragment() {

    private val mFlutter: FlutterFragment

    private var flutterFragment: FlutterFragment? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return FragmentScaffold(
            context = requireActivity()
        ).apply {
            addView(
                binding.root,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (flutterFragment == null) {
            flutterFragment = mFlutter
            childFragmentManager.beginTransaction()
                .add(
                    binding.flutterContainer.id,
                    mFlutter,
                    TAG_FLUTTER_FRAGMENT
                )
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        flutterFragment = null
    }

    companion object {

        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"

        fun newInstance(flutterFragment: FlutterFragment): HomeFragment{
            return HomeFragment(flutterFragment = flutterFragment)
        }
    }
}