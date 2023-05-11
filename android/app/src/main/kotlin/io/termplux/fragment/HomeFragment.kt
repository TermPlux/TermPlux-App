package io.termplux.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.idlefish.flutterboost.containers.FlutterBoostFragment

class HomeFragment : FlutterBoostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val flutter = super.onCreateView(inflater, container, savedInstanceState)
        return LinearLayoutCompat(requireActivity()).apply {
            addView(TextView(requireActivity()).apply {
                text = "fuck"
            })

            addView(
                flutter,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }
}