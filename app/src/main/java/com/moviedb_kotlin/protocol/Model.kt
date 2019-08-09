package com.moviedb_kotlin.protocol

import com.google.gson.annotations.SerializedName

open class Content(val id: Int,
                   @SerializedName("title", alternate = ["name"])
                   val title: String,
                   val overview: String?,
                   @SerializedName("poster_path")
                   val posterPath: String?,
                   @SerializedName("vote_average")
                   val rating: Double)

class Genre(val id: Int, val name: String)

class Creator(val id: Int, val name: String)

class Person(val id: Int, val name: String,
             val character: String,
             @SerializedName("profile_path")
             val profilePath: String)

class Movie(id: Int, title: String,
            overview: String, posterPath: String, rating: Double,
            val budget: Int,
            @SerializedName("release_date")
            val releaseDate: String,
            val runtime: Int?,
            val genres: Array<Genre>
            ): Content(id, title, overview, posterPath, rating)

class TvShow(id: Int, title: String,
             overview: String, posterPath: String, rating: Double,
             val status: String,
             val genres: Array<Genre>,
             @SerializedName("created_by")
             val creators: Array<Creator>,
             @SerializedName("first_air_date")
             val airDate: String
             ): Content(id, title, overview, posterPath, rating)