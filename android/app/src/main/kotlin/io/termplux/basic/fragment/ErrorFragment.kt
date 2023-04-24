package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import io.termplux.basic.custom.FragmentScaffold
import io.termplux.databinding.FragmentErrorBinding

class ErrorFragment constructor(viewPager: ViewPager2) : Fragment() {

    private var mViewPager: ViewPager2

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    init {
        mViewPager = viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return FragmentScaffold(
            context = requireActivity(),
            view = binding.root
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.error.setOnClickListener {
            navigationToHome()
        }
    }

    private fun navigationToHome() {
        mViewPager.setCurrentItem(0, true)
    }

    companion object {

        fun newInstance(viewPager: ViewPager2): ErrorFragment {
            return ErrorFragment(viewPager = viewPager)
        }
    }
}