package com.moviedb_kotlin.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Person(var id: Int = 0,
             var name: String = "",
             var character: String? = null,
             var posterPath: String? = null): RealmObject()

open class Content(@PrimaryKey var id: Int = 0,
                   var title: String = "",
                   var overview: String? = null,
                   var posterPath: String? = null,
                   var type: String = "",
                   var rating: Double = 0.0): RealmObject()