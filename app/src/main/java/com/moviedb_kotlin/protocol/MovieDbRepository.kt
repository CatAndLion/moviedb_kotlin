package com.moviedb_kotlin.protocol

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbRepository {

    @GET("/3/trending/{media_type}/{time_window}")
    fun getTrending(@Path("media_type") mediaType: String,
                    @Path("time_window") timeWindow: String,
                    @Query("api_key") apiKey: String,
                    @Query("page") page: Int): Single<ListResponse<Content>>

    @GET("/3/search/movie")
    fun searchMovies(@Query("api_key") apiKey: String,
                     @Query("page") page: Int,
                     @Query("query") query: String): Single<ListResponse<Content>>


    @GET("/3/search/tv")
    fun searchTvShows(@Query("api_key") apiKey: String,
                     @Query("page") page: Int,
                     @Query("query") query: String): Single<ListResponse<Content>>

    @GET("/3/movie/{movie_id}")
    fun getMovie(@Path("movie_id") id: Int,
                 @Query("api_key") apiKey: String): Single<Movie>

    @GET("/3/tv/{tv_id}")
    fun getTvShow(@Path("tv_id") id: Int,
                  @Query("api_key") apiKey: String): Single<TvShow>

    @GET("/3/{type}/{id}/credits")
    fun getCast(@Path("type") type: String,
                @Path("id") id: Int,
                @Query("api_key") apiKey: String): Single<CastResponse>
}