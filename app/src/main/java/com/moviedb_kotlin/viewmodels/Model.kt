package com.moviedb_kotlin.viewmodels

import com.moviedb_kotlin.protocol.*
import java.text.SimpleDateFormat
import java.util.*
import com.moviedb_kotlin.protocol.Content as pContent
import com.moviedb_kotlin.protocol.Person as pPerson
import com.moviedb_kotlin.database.Content as dbContent
import com.moviedb_kotlin.database.Person as dbPerson

enum class ContentType {
    Movie, TvShow;
}

class Person(val id: Int,
             val name: String,
             val character: String,
             val posterPath: String) {

    companion object {
        fun fromProtocol(item: pPerson): Person {
            return Person(item.id, item.name, item.character,
                "${MovieDbApi.imageUrl}${item.profilePath}")
        }

        fun fromDatabase(item: dbPerson): Person {
            return Person(item.id, item.name, item.character, item.posterPath)
        }
    }
}

open class Content(val id: Int,
                   val title: String,
                   val overview: String?,
                   val posterPath: String?,
                   val releaseDate: Date?,
                   val type: ContentType,
                   val rating: Double) {

    companion object {
        fun fromProtocol(item: pContent, type: ContentType): Content {
            return Content(item.id, item.title,
                item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                if(Regex("^\\d{4}-\\d{2}-\\d{2}\$").matches(item.releaseDate))
                    SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(item.releaseDate)
                else null,
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
                  releaseDate: Date?,
                  val budget: Int,
                  val genre: List<String>):
    com.moviedb_kotlin.viewmodels.Content(id, title, overview, posterPath, releaseDate, type, rating) {

    var cast: List<Person> = listOf()

    companion object {

        fun fromDatabase(item: dbContent): ContentFull {
            val cast = List(item.cast.size) {
                Person.fromDatabase(item.cast[it]!!)
            }
            val genre = List(item.genre.size) {
                item.genre[it]!!
            }
            return ContentFull(item.id, item.title, item.overview, item.posterPath,
                ContentType.values()[item.type], item.rating, Date(), item.budget,
                genre).apply {
                this.cast = cast
            }
        }

        fun fromProtocol(item: Movie): ContentFull {
            return ContentFull(item.id, item.title, item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                ContentType.Movie, item.rating,
                if(Regex("^\\d{4}-\\d{2}-\\d{2}\$").matches(item.releaseDate))
                    SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(item.releaseDate)
                else null,
                item.budget, item.genres.map { it.name })
        }

        fun fromProtocol(item: TvShow): ContentFull {
            return ContentFull(item.id, item.title, item.overview,
                "${MovieDbApi.imageUrl}${item.posterPath}",
                ContentType.Movie, item.rating,
                if(Regex("^\\d{4}-\\d{2}-\\d{2}\$").matches(item.releaseDate))
                    SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(item.releaseDate)
                else null,
                0, item.genres.map { it.name })
        }
    }
}