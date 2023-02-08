package io.termplux.adapter

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import io.termplux.R

class MainAdapter(
    private var appsList: List<ResolveInfo>,
    private val context: Context,
) : BaseAdapter() {

    override fun getCount(): Int {
        return appsList.size
    }

    override fun getItem(i: Int): Any {
        return appsList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        return LinearLayoutCompat(context).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                context.resources.getDimension(R.dimen.app_width).toInt(),
                context.resources.getDimension(R.dimen.app_height).toInt()
            )
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppCompatImageView(context).apply {
                    setImageDrawable(appsList[i].activityInfo.loadIcon(context.packageManager))
                    setPadding(context.resources.getDimension(R.dimen.icon_padding).toInt())
                    gravity = Gravity.CENTER_HORIZONTAL
                },
                LinearLayoutCompat.LayoutParams(
                    context.resources.getDimension(R.dimen.icon_size).toInt(),
                    context.resources.getDimension(R.dimen.icon_size).toInt()
                )
            )
            addView(
                AppCompatTextView(context).apply {
                    gravity = Gravity.CENTER
                    text = appsList[i].loadLabel(context.packageManager)
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }
}