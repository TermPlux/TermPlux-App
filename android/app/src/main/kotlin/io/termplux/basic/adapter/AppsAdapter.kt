package io.termplux.basic.adapter

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.AppUtils
import com.google.android.material.snackbar.Snackbar
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import io.termplux.BuildConfig
import io.termplux.R
import io.termplux.model.AppsModel

class AppsAdapter constructor(
    applicationList: List<AppsModel>,
    viewPager: ViewPager2
) : RecyclerView.Adapter<AppsViewHolder>() {

    private var mApplicationList: List<AppsModel>
    private var mViewPager: ViewPager2

    init {
        mApplicationList = applicationList
        mViewPager = viewPager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {

        val appIcon = AppCompatImageView(parent.context)
        val appTitle = AppCompatTextView(parent.context)

//        val itemView: LinearLayoutCompat = LinearLayoutCompat(
//            parent.context
//        ).apply {
//            layoutParams = LinearLayoutCompat.LayoutParams(
//                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                parent.context.resources.getDimension(
//                    R.dimen.launcher_height
//                ).toInt()
//            )
//            orientation = LinearLayoutCompat.HORIZONTAL
//            addView(
//                appIcon.apply {
//                    gravity = Gravity.CENTER_VERTICAL
//                    setPadding(
//                        parent.context.resources.getDimension(
//                            R.dimen.launcher_padding
//                        ).toInt(),
//                        parent.context.resources.getDimension(
//                            R.dimen.icon_padding
//                        ).toInt(),
//                        0,
//                        parent.context.resources.getDimension(
//                            R.dimen.icon_padding
//                        ).toInt()
//                    )
//                },
//                LinearLayoutCompat.LayoutParams(
//                    parent.context.resources.getDimension(
//                        R.dimen.icon_size
//                    ).toInt(),
//                    parent.context.resources.getDimension(
//                        R.dimen.icon_size
//                    ).toInt()
//                )
//            )
//            addView(
//                appTitle.apply {
//                    gravity = Gravity.CENTER_VERTICAL
//                    setPadding(
//                        parent.context.resources.getDimension(
//                            R.dimen.launcher_padding
//                        ).toInt(),
//                        0,
//                        parent.context.resources.getDimension(
//                            R.dimen.launcher_padding
//                        ).toInt(),
//                        0
//                    )
//                },
//                LinearLayoutCompat.LayoutParams(
//                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
//                )
//            )
//        }


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
                    gravity = Gravity.CENTER_HORIZONTAL
                    setPadding(
                        parent.context.resources.getDimension(
                            R.dimen.icon_padding
                        ).toInt()
                    )
                },
                LinearLayoutCompat.LayoutParams(
                    parent.context.resources.getDimension(
                        R.dimen.icon_size
                    ).toInt(),
                    parent.context.resources.getDimension(
                        R.dimen.icon_size
                    ).toInt()
                )
            )
            addView(
                appTitle.apply {
                    gravity = Gravity.CENTER
                    setTextColor(Color.WHITE)
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

        // 设置应用图标
        holder.mAppIconView.apply {
            setImageDrawable(
                AppUtils.getAppIcon(
                    mApplicationList[
                            holder.absoluteAdapterPosition
                    ].pkgName
                )
            )
        }

        // 设置应用标题
        holder.mTextView.apply {
            // 最多一行
            maxLines = 1
            // 末尾添加省略号
            ellipsize = TextUtils.TruncateAt.END
            // 设置文本
            text = AppUtils.getAppName(
                mApplicationList[
                        holder.absoluteAdapterPosition
                ].pkgName
            )
        }

        holder.itemView.setOnLongClickListener { view ->
            PopMenu.show(
                if (
                    mApplicationList[
                            holder.absoluteAdapterPosition
                    ].pkgName != BuildConfig.APPLICATION_ID
                ) arrayOf(
                    view.context.getString(R.string.menu_open),
                    view.context.getString(R.string.menu_info),
                    view.context.getString(R.string.menu_delete)
                ) else arrayOf(
                    view.context.getString(R.string.menu_launcher),
                    view.context.getString(R.string.menu_home),
                    view.context.getString(R.string.menu_settings)
                )
            )
                .setOnIconChangeCallBack(
                    object : OnIconChangeCallBack<PopMenu?>(true) {
                        override fun getIcon(
                            dialog: PopMenu?,
                            index: Int,
                            menuText: String?
                        ): Int {
                            return if (
                                mApplicationList[
                                        holder.absoluteAdapterPosition
                                ].pkgName != BuildConfig.APPLICATION_ID
                            ) when (index) {
                                open -> R.drawable.outline_open_in_new_24
                                info -> R.drawable.outline_info_24
                                delete -> R.drawable.outline_delete_24
                                else -> 0
                            } else when (index) {
                                open -> R.drawable.outline_rocket_launch_24
                                info -> R.drawable.outline_home_24
                                delete -> R.drawable.outline_settings_24
                                else -> 0
                            }
                        }
                    }
                )
                .setOnMenuItemClickListener { _, _, index ->
                    if (mApplicationList[
                                holder.absoluteAdapterPosition
                        ].pkgName != BuildConfig.APPLICATION_ID
                    ) when (index) {
                        open -> openApp(view = view, position = position)
                        info -> infoApp(position = position)
                        delete -> deleteApp(view = view, position = position)
                    } else when (index) {
                        open -> navToLauncher()
                        info -> navToHome()
                        delete -> navToSettings()
                    }
                    false
                }
            true
        }

        holder.itemView.setOnClickListener { view ->
            if (mApplicationList[
                        holder.absoluteAdapterPosition
                ].pkgName != BuildConfig.APPLICATION_ID
            ) {
                openApp(view = view, position = position)
            } else {
                navToHome()
            }
        }
    }

    override fun getItemCount(): Int {
        return mApplicationList.size
    }

    private fun openApp(view: View, position: Int) {
        if (
            view.context.packageManager.getLaunchIntentForPackage(
                mApplicationList[position].pkgName
            ) != null
        ) AppUtils.launchApp(mApplicationList[position].pkgName) else {
            val error = view.context.getString(R.string.error_could_not_start)
            try {
                PopTip.show(error)
            } catch (e: Exception) {
                val errors = error + Log.getStackTraceString(e)
                Snackbar.make(view.context, view, errors, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun infoApp(position: Int) {
        AppUtils.launchAppDetailsSettings(mApplicationList[position].pkgName)
    }

    private fun deleteApp(view: View, position: Int) {
        if (!AppUtils.isAppSystem(mApplicationList[position].pkgName)) {
            AppUtils.uninstallApp(mApplicationList[position].pkgName)
        } else {
            val error = view.context.getString(R.string.uninstall_error)
            try {
                PopTip.show(error)
            } catch (e: Exception) {
                val errors = error + Log.getStackTraceString(e)
                Snackbar.make(view.context, view, errors, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun navToLauncher() {
        mViewPager.currentItem = ContentAdapter.launcher
    }

    private fun navToHome() {
        mViewPager.currentItem = ContentAdapter.home
    }

    private fun navToSettings() {
        mViewPager.currentItem = ContentAdapter.settings
    }

    companion object {

        fun newInstance(
            applicationList: List<AppsModel>,
            viewPager: ViewPager2
        ): AppsAdapter {
            return AppsAdapter(
                applicationList = applicationList,
                viewPager = viewPager
            )
        }

        const val open: Int = 0
        const val info: Int = 1
        const val delete: Int = 2
    }
}