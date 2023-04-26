package io.termplux.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.termplux.app.ui.navigation.Screen
import io.termplux.basic.adapter.ContentAdapter
import io.termplux.basic.custom.FragmentScaffold
import io.termplux.basic.settings.Settings
import io.termplux.databinding.FragmentSettingsBinding

class SettingsFragment constructor(
    navigation: (String) -> Unit
) : Fragment() {

    private val mSettings: Settings
    private var mSettingsFragment: Settings? = null
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
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
        if (mSettingsFragment == null) {
            mSettingsFragment = mSettings
            childFragmentManager.beginTransaction()
                .add(
                    binding.settingsContainer.id,
                    mSettings,
                    TAG_SETTINGS_FRAGMENT
                )
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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


