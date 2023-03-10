package io.termplux.fragment

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.termplux.Apps
import io.termplux.adapter.AppsAdapter
import io.termplux.receiver.MainReceiver

class AppFragment(viewPager: ViewPager2) : Fragment() {

    private var mViewPager: ViewPager2

    private lateinit var recyclerView: RecyclerView
    private lateinit var appReceiver: BroadcastReceiver

    init {
        mViewPager = viewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recyclerView = RecyclerView(
            requireActivity()
        ).apply {
            layoutManager = GridLayoutManager(
                requireActivity(),
                4,
                GridLayoutManager.VERTICAL,
                false
            )
        }
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadApp()
        receiveSysCast()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(appReceiver)
    }

    private fun receiveSysCast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED")
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED")
        intentFilter.addDataScheme("package")

        appReceiver = MainReceiver(
            refresh = {
                loadApp()
            }
        )

        requireActivity().registerReceiver(appReceiver, intentFilter)
    }

    @Suppress("DEPRECATION")
    private fun loadApp() {

        // ????????????
        val applicationList: MutableList<Apps> = ArrayList()

        // ????????????
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        // ???????????????
        val getList = requireActivity().packageManager.queryIntentActivities(intent, 0)

        // ??????????????????
        for (resolveInfo in getList) {
            applicationList.add(
                Apps(
                    appIcon = resolveInfo.activityInfo.loadIcon(requireActivity().packageManager),
                    appLabel = resolveInfo.activityInfo.loadLabel(requireActivity().packageManager),
                    pkgName = resolveInfo.activityInfo.packageName
                )
            )
        }

        // ???????????????
        recyclerView.adapter = AppsAdapter(
            applicationList = applicationList,
            viewPager = mViewPager
        )
    }

    companion object {
        fun newInstance(viewPager: ViewPager2): AppFragment {
            return AppFragment(viewPager = viewPager)
        }
    }
}