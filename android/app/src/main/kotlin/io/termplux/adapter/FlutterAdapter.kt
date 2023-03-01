package io.termplux.adapter

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kongzue.baseframework.BaseActivity
import io.flutter.embedding.android.FlutterFragment

class FlutterAdapter(
    activity: BaseActivity,
    flutterFragment: FlutterFragment
) : FragmentStateAdapter(
    activity
) {

    private val flutter: FlutterFragment

    init {
        flutter = flutterFragment
    }

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): FlutterFragment = flutter
}