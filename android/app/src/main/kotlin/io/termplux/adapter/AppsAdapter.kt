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

class AppsAdapter(
    list: List<ResolveInfo>,
    context: Context
) : BaseAdapter() {

    private val appsList: List<ResolveInfo>
    private val viewContext: Context

    init {
        appsList = list
        viewContext = context
    }

    override fun getCount(): Int = appsList.size
    override fun getItem(position: Int): Any = appsList[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        return LinearLayoutCompat(
            viewContext
        ).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                viewContext.resources.getDimension(R.dimen.app_width).toInt(),
                viewContext.resources.getDimension(R.dimen.app_height).toInt()
            )
            orientation = LinearLayoutCompat.VERTICAL
            addView(
                AppCompatImageView(
                    viewContext
                ).apply {
                    setImageDrawable(
                        appsList[position].activityInfo.loadIcon(viewContext.packageManager)
                    )
                    setPadding(viewContext.resources.getDimension(R.dimen.icon_padding).toInt())
                },
                LinearLayoutCompat.LayoutParams(
                    viewContext.resources.getDimension(R.dimen.icon_size).toInt(),
                    viewContext.resources.getDimension(R.dimen.icon_size).toInt()
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            )
            addView(
                AppCompatTextView(
                    viewContext
                ).apply {
                    gravity = Gravity.CENTER
                    text = appsList[position].loadLabel(viewContext.packageManager)
                },
                LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }
}