package com.moviedb_kotlin.loaders

import com.moviedb_kotlin.viewmodels.ContentFull
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmList

import com.moviedb_kotlin.database.Content as RealmContent
import com.moviedb_kotlin.database.Person as RealmPerson

class DbLoader(val realm: Realm) {

    fun getContent(id: Int, type: Int): Maybe<RealmContent> {

        return Maybe.create {
            val result = realm.where(RealmContent::class.java)
                .equalTo("id", id).and()
                .equalTo("type", type).findFirst()
            if(result != null) {
                it.onSuccess(result)
            }
            it.onComplete()
        }
    }

    fun hasContentOfId(id: Int): Single<Boolean> {
        return Single.create {
            val count = realm.where(RealmContent::class.java).equalTo("id", id).count()
            it.onSuccess(count > 0)
        }
    }

    fun getCast(id: Int): Single<List<RealmPerson>> {
        return Single.create {
            val ids = realm.where(RealmContent::class.java).equalTo("id", id).findAll().map {
                it.id
            }.toTypedArray()
            val cast = realm.where(RealmPerson::class.java).`in`("id", ids).findAll()
            it.onSuccess(cast)
        }
    }

    fun saveContent(item: ContentFull): Completable {
        return Completable.create {

            val genre = RealmList<String>()
            genre.addAll(item.genre)

            val cast = RealmList<RealmPerson>()
            cast.addAll(item.cast.map { p ->
                RealmPerson(p.id, p.name, p.character, p.posterPath)
            })

            realm.executeTransaction { db ->
                db.copyToRealmOrUpdate(RealmContent(
                    item.id, item.title,
                    item.type.ordinal,
                    item.overview,
                    item.posterPath,
                    item.releaseDate.toString(),
                    item.budget,
                    cast, genre,
                    item.rating
                ))
            }
            it.onComplete()
        }
    }
}