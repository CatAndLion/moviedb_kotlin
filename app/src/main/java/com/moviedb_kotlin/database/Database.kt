package com.moviedb_kotlin.database

import io.realm.Realm
import io.realm.RealmConfiguration

object Database {

    const val dbName = "localDb"

    val realm: Realm by lazy {
        create()
    }

    private fun create(): Realm {
        val config = RealmConfiguration.Builder()
            .name(dbName).build()
        return Realm.getInstance(config)
    }

}