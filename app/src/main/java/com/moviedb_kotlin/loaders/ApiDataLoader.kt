package com.moviedb_kotlin.loaders

import android.text.TextUtils
import com.moviedb_kotlin.protocol.*
import io.reactivex.Single

class ApiDataLoader {

    companion object {
        const val pageSize = 20 //api uses fixed page size = 20
        const val movieType = "movie"
        const val tvType = "tv"
    }

    fun getContentOfType(limit: Int, offset: Int, type: String, query: String?): Single<ListResponse<Content>> {
        val page = (offset / pageSize) + 1
        val call = if(TextUtils.isEmpty(query))
            MovieDbApi.api.getTrending(type, "week", MovieDbApi.apiKey, page)
        else
            when(type) {
                movieType -> MovieDbApi.api.searchMovies(MovieDbApi.apiKey, page, query!!)
                tvType -> MovieDbApi.api.searchTvShows(MovieDbApi.apiKey, page, query!!)
                else -> Single.just(ListResponse(0, 0, 0, arrayOf()))
            }
        return call
    }

    fun getMovie(id: Int): Single<Movie> {
        return MovieDbApi.api.getMovie(id, MovieDbApi.apiKey)
    }

    fun getTvShow(id: Int): Single<TvShow> {
        return MovieDbApi.api.getTvShow(id, MovieDbApi.apiKey)
    }

    fun getCasting(id: Int, type: String): Single<List<Person>> {
        return MovieDbApi.api.getCast(type, id, MovieDbApi.apiKey).map {
            it.cast.toList()
        }
    }
}