package io.termplux.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kongzue.baseframework.BaseActivity
import io.flutter.embedding.android.FlutterFragment

class FlutterAdapter(
    activity: BaseActivity,
    position: Int,
    flutterFragment: FlutterFragment
) : FragmentStateAdapter(
    activity
) {

    private val count: Int
    private val flutter: FlutterFragment

    init {
        count = position
        flutter = flutterFragment
    }

    override fun getItemCount(): Int = count

    override fun createFragment(position: Int): Fragment = flutter
}