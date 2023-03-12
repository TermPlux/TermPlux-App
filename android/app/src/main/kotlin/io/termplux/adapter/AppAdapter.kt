package io.termplux.adapter

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import io.termplux.Apps
import io.termplux.BuildConfig
import io.termplux.R

class AppAdapter(
    applicationList: List<Apps>,
    viewPager: ViewPager2
) : RecyclerView.Adapter<AppViewHolder>() {

    private var mApplicationList: List<Apps>
    private var mViewPager: ViewPager2

    init {
        mApplicationList = applicationList
        mViewPager = viewPager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {

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

        return AppViewHolder(item = itemView, icon = appIcon, title = appTitle)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        holder.mAppIconView.setImageDrawable(mApplicationList[position].appIcon)
        holder.mTextView.text = mApplicationList[position].appLabel

        holder.itemView.setOnLongClickListener { view ->
            PopMenu.show(arrayOf("打开", "应用信息", "卸载"))
                .setOnMenuItemClickListener { _, _, index ->
                    when (index) {
                        open -> openApp(position = position, view = view)
                        info -> infoApp(position = position, view = view)
                        delete -> deleteApp(holder = holder, position = position, view = view)
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

        holder.itemView.setOnClickListener { view ->
            openApp(position = position, view = view)
        }
    }

    override fun getItemCount(): Int {
        return mApplicationList.size
    }

    private fun openApp(position: Int, view: View) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            mApplicationList[position].appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(mApplicationList[position].appIntent)
        } else {
            // 跳转主页
            mViewPager.setCurrentItem(0, true)
        }
    }

    private fun infoApp(position: Int, view: View) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts(
                "package",
                mApplicationList[position].pkgName,
                null
            )
            view.context.startActivity(intent)
        } else {
            // 跳转关于
            mViewPager.setCurrentItem(0, true)
        }
    }

    private fun deleteApp(holder: AppViewHolder, position: Int, view: View) {
        if (mApplicationList[position].pkgName != BuildConfig.APPLICATION_ID) {
            if (mApplicationList[position].isSystemApp) {
                Snackbar.make(
                    holder.itemView,
                    "无法卸载系统应用",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val uri = Uri.fromParts(
                    "package",
                    mApplicationList[position].pkgName,
                    null
                )
                val intent = Intent(Intent.ACTION_DELETE, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                view.context.startActivity(intent)
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