package io.ecosed.droid.holder

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView

internal class AppsViewHolder constructor(
    item: LinearLayoutCompat,
    icon: AppCompatImageView,
    title: AppCompatTextView
) : RecyclerView.ViewHolder(item) {

    internal val mAppIconView: AppCompatImageView
    internal val mTextView: AppCompatTextView

    init {
        mAppIconView = icon
        mTextView = title
    }
}