package io.termplux.basic.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.basic.adapter.AppsAdapter
import io.termplux.basic.receiver.AppsReceiver
import io.termplux.model.AppsModel
import java.util.*

class AppsFragment constructor(
    viewPager: ViewPager2,
    navigation: (String) -> Unit
) : Fragment() {

    /** ViewPager2实例 */
    private var mViewPager: ViewPager2

    /** Compose导航函数 */
    private var mNavigation: (String) -> Unit

    private lateinit var mContext: Context
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var appReceiver: BroadcastReceiver

    init {
        mViewPager = viewPager
        mNavigation = navigation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 获取上下文
        mContext = requireActivity()
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        mRecyclerView = RecyclerView(
            requireActivity()
        ).apply {
//            layoutManager = GridLayoutManager(
//                mContext,
//                4,
//                RecyclerView.VERTICAL,
//                false
//            )
            layoutManager = LinearLayoutManager(
                mContext,
                RecyclerView.VERTICAL,
                false
            )
        }

       // return mRecyclerView

        return LinearLayoutCompat(
            requireActivity()
        ).apply {
            // 设置背景
            background = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.custom_wallpaper_24
            )
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppBarLayout(
                    requireActivity()
                ).apply {
                    addView(
                        MaterialToolbar(
                            requireActivity()
                        ).apply {
                            title = getString(R.string.menu_apps)
                            navigationIcon = ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.baseline_arrow_back_24
                            )
                        },
                        LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        )
                    )
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
            addView(
                mRecyclerView,
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 加载应用列表
        loadApp()

        // 意图过滤器
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")

        // 用于刷新应用列表的广播接收器
        appReceiver = AppsReceiver(
            refresh = {
                loadApp()
            }
        )

        // 注册广播接收器
        mContext.registerReceiver(appReceiver, intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 注销广播接收器
        mContext.unregisterReceiver(appReceiver)
    }

    @Suppress("DEPRECATION")
    private fun loadApp() {
        // 应用列表
        val applicationList: MutableList<AppsModel> = ArrayList()

        // 添加自己
        applicationList.add(
            AppsModel(pkgName = BuildConfig.APPLICATION_ID)
        )

        // 获取启动器列表
        for (resolveInfo in mContext.packageManager.queryIntentActivities(
            Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0
        )) {
            val pkg = resolveInfo.activityInfo.packageName
            if (packageFilter(pkg = pkg)) {
                applicationList.add(
                    AppsModel(pkgName = pkg)
                )
            }
        }

        // 设置适配器
        mRecyclerView.apply {
            adapter = AppsAdapter.newInstance(
                applicationList = applicationList,
                viewPager = mViewPager
            )
        }
    }

    /**
     * 包过滤器 - 过滤不必要应用
     */
    private fun packageFilter(pkg: String): Boolean {
        return pkg != BuildConfig.APPLICATION_ID
    }

    companion object {

        // 初始化函数
        fun newInstance(
            viewPager: ViewPager2,
            navigation: (String) -> Unit
        ): AppsFragment {
            return AppsFragment(
                viewPager = viewPager,
                navigation = navigation
            )
        }
    }
}