package io.ecosed.libecosed.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.ecosed.libecosed.R

internal class ContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = NavigationView(requireContext()).apply {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as NavigationView).apply {
            inflateHeaderView(R.layout.nav_header_main)
            inflateMenu(R.menu.activity_main_drawer)
//            setupWithNavController(
//                navController = findNavController()
//            )
        }
    }
}