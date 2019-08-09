package com.moviedb_kotlin.viewmodels

import com.moviedb_kotlin.protocol.*
import com.moviedb_kotlin.protocol.Content as pContent
import com.moviedb_kotlin.protocol.Person as pPerson

enum class ContentType {
    Movie, TvShow;
}

class Person(val name: String,
             val character: String,
             val posterPath: String) {

    companion object {
        fun fromProtocol(item: pPerson): Person {
            return Person(item.name, item.character, "${MovieDbApi.imageUrl}${item.profilePath}")
        }
    }
}

open class Content(val id: Int,
                   val title: String,
                   val overview: String?,
                   val posterPath: String?,
                   val type: ContentType,
                   val rating: Double) {

    companion object {
        fun fromProtocol(item: pContent, type: ContentType): Content {
            return Content(item.id, item.title,
                item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                type, item.rating)
        }
    }
}

class ContentFull(id: Int,
                  title: String,
                  overview: String?,
                  posterPath: String?,
                  type: ContentType,
                  rating: Double,
                  val releaseDate: String,
                  val budget: Int,
                  val status: String,
                  val genre: List<String>):
    com.moviedb_kotlin.viewmodels.Content(id, title, overview, posterPath, type, rating) {

    var cast: List<Person> = listOf()

    companion object {

        fun fromProtocol(item: Movie): ContentFull {
            return ContentFull(item.id, item.title, item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                ContentType.Movie, item.rating, item.releaseDate,
                item.budget, "", item.genres.map { it.name })
        }

        fun fromProtocol(item: TvShow): ContentFull {
            return ContentFull(item.id, item.title, item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                ContentType.Movie, item.rating, item.airDate,
                0, item.status, item.genres.map { it.name })
        }
    }
}