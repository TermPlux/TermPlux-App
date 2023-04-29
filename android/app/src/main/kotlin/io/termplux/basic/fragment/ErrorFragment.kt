package io.termplux.basic.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import io.termplux.R
import io.termplux.basic.adapter.ContentAdapter

class ErrorFragment constructor(
    current: (Int) -> Unit,
) : Fragment() {

    private val mCurrent: (Int) -> Unit
    private lateinit var textView: AppCompatTextView

    init {
        mCurrent = current
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
        mCurrent(ContentAdapter.home)
    }

    companion object {

        fun newInstance(
            current: (Int) -> Unit
        ): ErrorFragment {
            return ErrorFragment(
                current = current
            )
        }
    }
}