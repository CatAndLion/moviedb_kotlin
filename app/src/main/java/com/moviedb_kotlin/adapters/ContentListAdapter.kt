package com.moviedb_kotlin.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.moviedb_kotlin.R
import com.moviedb_kotlin.utils.GlideAppModule
import com.moviedb_kotlin.viewmodels.Content
import kotlinx.android.synthetic.main.content_details.view.*

import kotlinx.android.synthetic.main.content_item.view.*
import kotlinx.android.synthetic.main.content_item.view.overview
import kotlinx.android.synthetic.main.content_item.view.posterImage
import kotlinx.android.synthetic.main.content_item.view.rating
import kotlinx.android.synthetic.main.content_item.view.ratingBar
import kotlinx.android.synthetic.main.content_item.view.releaseDate
import kotlinx.android.synthetic.main.content_item.view.title
import java.text.SimpleDateFormat
import java.util.*
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

    companion object {
        fun getRatingColor(context: Context, rating: Double): Int {
            return ResourcesCompat.getColor(context.resources,
                if(rating < 3) R.color.ratingLow else if(rating < 6) R.color.ratingMedium else R.color.ratingHigh,
                null)
        }
    }

    fun fill(item: Content, listener: ItemClickListener?) {

        itemView.setOnClickListener {
            listener?.itemClicked(itemView, item)
        }

        itemView.title.text = item.title
        itemView.overview.text = item.overview

        itemView.releaseDate.text = item.releaseDate?.let {
            SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(item.releaseDate)
        }

        val rating = (item.rating * 10).roundToInt()
        itemView.rating.text = "$rating%"
        itemView.ratingBar.progress = rating

        itemView.ratingBar.progressTintList = ColorStateList.valueOf(getRatingColor(itemView.context, item.rating))

        if(!TextUtils.isEmpty(item.posterPath)) {
            GlideAppModule.getRequest(itemView.posterImage, GlideAppModule.CacheOptions.Memory)
                .load(item.posterPath)
                .error(R.drawable.movie_poster_stub)
                .into(itemView.posterImage)

        } else {
            itemView.posterImage.setImageResource(R.drawable.no_poster)
        }

        ViewCompat.setTransitionName(itemView.title, item.id.toString())
    }
}

interface ItemClickListener {
    fun itemClicked(view: View, item: Content)
}