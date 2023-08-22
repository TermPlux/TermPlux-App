package io.ecosed.libecosed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView

class EmptyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return MaterialTextView(requireContext()).apply {
            text = "Empty."
        }
    }

    companion object {
        fun newInstance(): EmptyFragment{
            return EmptyFragment()
        }
    }
}