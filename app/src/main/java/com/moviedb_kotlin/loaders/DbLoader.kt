package com.moviedb_kotlin.loaders

import com.moviedb_kotlin.viewmodels.Content
import com.moviedb_kotlin.viewmodels.ContentType
import io.reactivex.Single

class DbLoader {

    /*
    override fun getContentOfType(limit: Int, offset: Int, type: ContentType, query: String?): Single<PagedData<Content>> {

        return Single.just(PagedData(0, listOf()))
    }
    */

    /*
    fun saveContent(data: List<Content>): Completable {
        return Completable.create {
            Database.realm.executeTransaction { db ->
                db.copyToRealmOrUpdate(data)
            }
            it.onComplete()
        }
    }
    */
}