package io.termplux.adapter

import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import io.termplux.Apps
import io.termplux.BuildConfig
import io.termplux.R

class AppsAdapter(
    applicationList: List<Apps>,
    viewPager: ViewPager2
) : RecyclerView.Adapter<AppsViewHolder>() {

    private var mApplicationList: List<Apps>
    private var mViewPager: ViewPager2

    init {
        mApplicationList = applicationList
        mViewPager = viewPager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {

        val appIcon = AppCompatImageView(parent.context)
        val appTitle = AppCompatTextView(parent.context)

        val itemView: LinearLayoutCompat = LinearLayoutCompat(
            parent.context
        ).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                parent.context.resources.getDimension(R.dimen.app_width).toInt(),
                parent.context.resources.getDimension(R.dimen.app_height).toInt()
            )
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                appIcon.apply {
                    setPadding(
                        parent.context.resources.getDimension(
                            R.dimen.icon_padding
                        ).toInt()
                    )
                },
                LinearLayoutCompat.LayoutParams(
                    parent.context.resources.getDimension(R.dimen.icon_size).toInt(),
                    parent.context.resources.getDimension(R.dimen.icon_size).toInt()
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            )
            addView(
                appTitle.apply {
                    gravity = Gravity.CENTER
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
        }

        return AppsViewHolder(item = itemView, icon = appIcon, title = appTitle)
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {

        holder.mAppIconView.setImageDrawable(mApplicationList[position].appIcon)
        holder.mTextView.text = mApplicationList[position].appLabel

        holder.itemView.setOnLongClickListener {
            PopMenu.show(arrayOf("打开", "应用信息", "卸载"))
                .setOnMenuItemClickListener { _, _, index ->
                    when (index) {
                        open -> openApp(position = position)
                        info -> infoApp(position = position)
                        delete -> deleteApp(position = position)
                    }
                    false
                }
                .onIconChangeCallBack = object : OnIconChangeCallBack<PopMenu?>(true) {
                override fun getIcon(dialog: PopMenu?, index: Int, menuText: String): Int {
                    return when (index) {
                        open -> R.drawable.outline_open_in_new_24
                        info -> R.drawable.outline_info_24
                        delete -> R.drawable.outline_delete_24
                        else -> 0
                    }
                }
            }
            true
        }

        holder.itemView.setOnClickListener {
            openApp(position = position)
        }
    }

    override fun getItemCount(): Int {
        return mApplicationList.size
    }

    private fun openApp(position: Int) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            AppUtils.launchApp(mApplicationList[position].pkgName)
        } else {
            // 跳转主页
            mViewPager.setCurrentItem(0, true)
        }
    }

    private fun infoApp(position: Int) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            AppUtils.launchAppDetailsSettings(mApplicationList[position].pkgName)
        } else {
            // 跳转关于
            mViewPager.setCurrentItem(0, true)
        }
    }

    private fun deleteApp(position: Int) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            if (!AppUtils.isAppSystem(mApplicationList[position].pkgName)) {
                AppUtils.uninstallApp(mApplicationList[position].pkgName)
            } else {
                PopTip.show("无法卸载系统应用")
            }
        } else {
            // 跳转设置卸载应用
            mViewPager.setCurrentItem(0, true)
        }
    }

    companion object {
        const val open: Int = 0
        const val info: Int = 1
        const val delete: Int = 2
    }
}