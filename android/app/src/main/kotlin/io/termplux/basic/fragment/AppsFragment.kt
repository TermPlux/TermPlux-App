package io.termplux.basic.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.basic.adapter.AppsAdapter
import io.termplux.basic.receiver.AppsReceiver
import io.termplux.model.AppsModel
import java.util.*

class AppsFragment constructor(
    current: (Int) -> Unit
) : Fragment() {

    /** ViewPager2实例 */
    private val mCurrent: (Int) -> Unit

    private lateinit var mContext: Context
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var appReceiver: BroadcastReceiver

    init {
        mCurrent = current
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
            layoutManager = GridLayoutManager(
                mContext,
                4,
                RecyclerView.VERTICAL,
                false
            )
        }

        return FrameLayout(
            requireActivity()
        ).apply {
            // 设置背景
            background = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.custom_wallpaper_24
            )
            addView(
                mRecyclerView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
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
                current = mCurrent
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
            current: (Int) -> Unit
        ): AppsFragment {
            return AppsFragment(
                current = current
            )
        }
    }
}