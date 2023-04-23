package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import io.termplux.R
import io.termplux.basic.adapter.ContentAdapter
import io.termplux.app.ui.navigation.Screen

class NavigationFragment constructor(
    viewPager: ViewPager2,
    navigation: (String) -> Unit
) : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val mViewPager: ViewPager2
    private val mNavigation: (String) -> Unit

    private lateinit var mNavigationView: NavigationView

    init {
        mViewPager = viewPager
        mNavigation = navigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mNavigationView = NavigationView(requireActivity()).apply {
            inflateHeaderView(R.layout.nav_header_main)
            inflateMenu(R.menu.navigation)
        }
        return mNavigationView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavigationView.apply {
            setNavigationItemSelectedListener(this@NavigationFragment)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_launcher -> mViewPager.currentItem = ContentAdapter.apps
            R.id.nav_home -> mViewPager.currentItem = ContentAdapter.home
            R.id.nav_navigation -> mViewPager.currentItem = ContentAdapter.nav
            R.id.nav_settings -> mViewPager.currentItem = ContentAdapter.settings
        }
        return false
    }

    companion object {
        fun newInstance(
            viewPager: ViewPager2,
            navigation: (String) -> Unit
        ): NavigationFragment {
            return NavigationFragment(
                viewPager = viewPager,
                navigation = navigation
            )
        }
    }
}