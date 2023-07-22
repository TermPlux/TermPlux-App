package io.termplux.app.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import io.termplux.app.R

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.setStorageDeviceProtected()
        //preferenceManager.sharedPreferencesName = ShizukuSettings.NAME
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val full = findPreference<TwoStatePreference>("full_mode")

        full?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _: Preference?, value: Any? ->
//                if (value is Boolean) {
//                    if (ThemeHelper.isUsingSystemColor() != value) {
//                        activity?.recreate()
//                    }
//                }
                true
            }

    }

//    override fun onCreateRecyclerView(
//        inflater: LayoutInflater,
//        parent: ViewGroup,
//        savedInstanceState: Bundle?
//    ): RecyclerView {
//        val recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState) as BorderRecyclerView
//        recyclerView.fixEdgeEffect()
//        recyclerView.addEdgeSpacing(bottom = 8f, unit = TypedValue.COMPLEX_UNIT_DIP)
//
//        val lp = recyclerView.layoutParams
//        if (lp is FrameLayout.LayoutParams) {
//            lp.rightMargin = recyclerView.context.resources.getDimension(R.dimen.rd_activity_horizontal_margin).toInt()
//            lp.leftMargin = lp.rightMargin
//        }
//
//        return recyclerView
//    }

    companion object {
        fun newInstance(): PreferenceFragment{
            return PreferenceFragment()
        }
    }
}