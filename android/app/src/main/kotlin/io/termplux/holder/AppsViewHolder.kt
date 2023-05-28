package io.termplux.holder

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView

class AppsViewHolder constructor(
    item: LinearLayoutCompat,
    icon: AppCompatImageView,
    title: AppCompatTextView
) : RecyclerView.ViewHolder(item) {

    val mAppIconView: AppCompatImageView
    val mTextView: AppCompatTextView

    init {
        mAppIconView = icon
        mTextView = title
    }
}