package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.R
import io.termplux.app.ui.navigation.Screen
import io.termplux.basic.settings.Settings

class SettingsFragment constructor(
    navigation: (String) -> Unit
) : Fragment() {

    private val mSettings: Settings
    private var mSettingsFragment: Settings? = null


    init {
        mSettings = Settings {
            navigation(
                Screen.Settings.route
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager: FragmentManager = childFragmentManager
        mSettingsFragment = fragmentManager.findFragmentByTag(TAG_SETTINGS_FRAGMENT) as Settings?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return  FragmentContainerView(
            requireActivity()
        ).apply {
            id = R.id.settings_container
        }
//        return LinearLayoutCompat(
//            requireActivity()
//        ).apply {
//            orientation = LinearLayoutCompat.VERTICAL
//            addView(
//                AppBarLayout(
//                    requireActivity()
//                ).apply {
//                    addView(
//                        MaterialToolbar(
//                            requireActivity()
//                        ).apply {
//                            title = getString(R.string.menu_settings)
//                            navigationIcon = ContextCompat.getDrawable(
//                                requireActivity(),
//                                R.drawable.baseline_arrow_back_24
//                            )
//                        },
//                        LinearLayoutCompat.LayoutParams(
//                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
//                        )
//                    )
//                },
//                LinearLayoutCompat.LayoutParams(
//                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
//                )
//            )
//            addView(
//                FragmentContainerView(
//                    requireActivity()
//                ).apply {
//                    id = R.id.settings_container
//                },
//                LinearLayoutCompat.LayoutParams(
//                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
//                )
//            )
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mSettingsFragment == null) {
            mSettingsFragment = mSettings
            childFragmentManager.beginTransaction()
                .add(
                    R.id.settings_container,
                    mSettings,
                    TAG_SETTINGS_FRAGMENT
                )
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSettingsFragment = null
    }

    companion object {
        fun newInstance(navigation: (String) -> Unit): SettingsFragment {
            return SettingsFragment(
                navigation = navigation
            )
        }

        private const val TAG_SETTINGS_FRAGMENT = "settings_fragment"
    }
}


