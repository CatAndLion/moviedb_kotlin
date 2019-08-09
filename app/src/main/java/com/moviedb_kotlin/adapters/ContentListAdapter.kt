package com.moviedb_kotlin.adapters

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.moviedb_kotlin.R
import com.moviedb_kotlin.utils.GlideAppModule
import com.moviedb_kotlin.viewmodels.Content

import kotlinx.android.synthetic.main.content_item.view.*
import kotlin.math.roundToInt

class ContentListAdapter(context: Context, limit: Int,
                         val listener: ItemClickListener?):
    PagedListAdapter<Content, ContentItemViewHolder>(context, limit) {

    override fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ContentItemViewHolder(inflater.inflate(R.layout.content_list_item, parent, false))
    }

    override fun bindItemViewHolder(viewHolder: ContentItemViewHolder, item: Content) {
        viewHolder.fill(item, listener)
    }

}

class ContentItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun fill(item: Content, listener: ItemClickListener?) {

        itemView.setOnClickListener {
            listener?.itemClicked(itemView, item)
        }

        itemView.title.text = item.title
        itemView.overview.text = item.overview

        val rating = (item.rating * 10).roundToInt()
        itemView.rating.text = "$rating%"
        itemView.ratingBar.progress = rating

        if(!TextUtils.isEmpty(item.posterPath)) {
            GlideAppModule.getRequest(itemView.posterImage, GlideAppModule.CacheOptions.Memory)
                .load(item.posterPath).into(itemView.posterImage)

        } else {
            itemView.posterImage.setImageResource(R.drawable.no_poster)
        }

        ViewCompat.setTransitionName(itemView.title, item.id.toString())
    }
}

interface ItemClickListener {
    fun itemClicked(view: View, item: Content)
}