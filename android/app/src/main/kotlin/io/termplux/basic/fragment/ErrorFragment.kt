package io.termplux.basic.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import io.termplux.R

class ErrorFragment constructor(viewPager: ViewPager2) : Fragment() {

    private var mViewPager: ViewPager2
    private lateinit var textView: AppCompatTextView

    init {
        mViewPager = viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        textView = AppCompatTextView(requireActivity()).apply {
            text = getString(R.string.error)
            textSize = 50f
            setTextColor(Color.RED)
        }
        return textView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.setOnClickListener {
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