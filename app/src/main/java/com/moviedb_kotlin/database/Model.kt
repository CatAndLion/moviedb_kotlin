package com.moviedb_kotlin.database

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Person(@PrimaryKey var id: Int = 0,
                  var name: String = "",
                  var character: String = "",
                  var posterPath: String = ""): RealmObject()

open class Content(@PrimaryKey var id: Int = 0,
                   var title: String = "",
                   var type: Int = 0,
                   var overview: String? = null,
                   var posterPath: String? = null,
                   var releaseDate: String = "",
                   var budget: Int = 0,
                   var cast: RealmList<Person> = RealmList(),
                   var genre: RealmList<String> = RealmList(),
                   var rating: Double = 0.0): RealmObject()