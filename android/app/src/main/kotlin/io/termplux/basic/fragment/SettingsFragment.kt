package io.termplux.basic.fragment

import android.view.View
import com.kongzue.baseframework.BaseFragment
import io.termplux.app.ui.navigation.Screen
import io.termplux.basic.activity.TermPluxActivity

class SettingsFragment constructor(
    navigation: (String) -> Unit
) : BaseFragment<TermPluxActivity>() {

    private val mNavigation: () -> Unit

    init {
        mNavigation = {
            navigation(
                Screen.Settings.route
            )
        }
    }

    override fun resetContentView(): View {
        return super.resetContentView()
    }

    override fun initViews() {

    }

    override fun initDatas() {

    }

    override fun setEvents() {

    }

    companion object {

        fun newInstance(
            navigation: (String) -> Unit
        ): SettingsFragment{
            return SettingsFragment(
                navigation = navigation
            )
        }
    }
}