package com.moviedb_kotlin.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviedb_kotlin.R
import androidx.transition.*
import com.moviedb_kotlin.adapters.CastListAdapter
import com.moviedb_kotlin.adapters.ContentItemViewHolder
import com.moviedb_kotlin.utils.GlideAppModule
import com.moviedb_kotlin.viewmodels.Content
import com.moviedb_kotlin.viewmodels.ContentDetailsViewModel
import com.moviedb_kotlin.viewmodels.ContentFull
import com.moviedb_kotlin.viewmodels.ContentType
import kotlinx.android.synthetic.main.content_details.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class ContentDetailsFragment: Fragment() {

    lateinit var viewModel: ContentDetailsViewModel
    lateinit var adapter: CastListAdapter

    companion object{
        fun instantinate(item: Content): ContentDetailsFragment {
            return ContentDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("ID", item.id)
                    putString("TITLE", item.title)
                    putString("POSTER", item.posterPath)
                    putString("TYPE", item.type.toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()

        viewModel = ViewModelProviders.of(this)[ContentDetailsViewModel::class.java]
        val type = arguments?.getString("TYPE") ?: ContentType.Movie.toString()
        viewModel.contentType = ContentType.valueOf(type)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_details, container, false)

        adapter = CastListAdapter(context!!)
        view.castList.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        view.castList.adapter = adapter

        //set title
        val title = arguments?.getString("TITLE")
        view.title.text = title

        //set poster
        val posterPath = arguments?.getString("POSTER")
        if(!TextUtils.isEmpty(posterPath)) {
            GlideAppModule.getRequest(context!!, GlideAppModule.CacheOptions.Memory)
                .load(posterPath).into(view.posterImage)
        }

        //loading changed
        viewModel.isLoading.observe(this, Observer<Boolean> { value ->
            view.rating.visibility = if(!value) View.VISIBLE else View.INVISIBLE
            //view.ratingBar.isIndeterminate = !value

            view.overview.visibility = if(!value) View.VISIBLE else View.GONE
            view.genre.visibility = if(!value) View.VISIBLE else View.GONE
        })

        //content changed
        viewModel.content.observe(this, Observer<ContentFull> { item ->
            view.overview.text = item.overview

            view.releaseDate.text = item.releaseDate?.let {
                SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(item.releaseDate)
            }

            val rating = (item.rating * 10).roundToInt()
            view.rating.text = "$rating%"
            view.ratingBar.progress = rating

            view.ratingBar.progressTintList = ColorStateList.valueOf(ContentItemViewHolder
                .getRatingColor(context!!, item.rating))

            view.genre.text = TextUtils.join(", ", item.genre)

            if(!item.cast.isEmpty()) {
                adapter.addData(item.cast)
                adapter.notifyDataSetChanged()
            }
        })

        if(viewModel.content.value == null) {
            viewModel.loadData(arguments?.getInt("ID") ?: 0)
        }

        return view
    }
}