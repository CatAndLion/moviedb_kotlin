package com.moviedb_kotlin.command

import com.moviedb_kotlin.loaders.ApiDataLoader
import com.moviedb_kotlin.viewmodels.ContentType
import com.moviedb_kotlin.viewmodels.Person
import io.reactivex.Observable

class GetCastCommand(val id: Int, val type: ContentType): Command<List<Person>>() {

    override fun getCall(): Observable<List<Person>> {
        return ApiDataLoader().getCasting(id, toProtocolType(type)).map { response ->
            response.map { Person.fromProtocol(it) }
        }.toObservable()
    }

    override fun getCompleteState(response: List<Person>): BaseState {
        return ListState(response, response.size)
    }

    val toProtocolType: (type: ContentType) -> String = {
        when(it) {
            ContentType.Movie -> ApiDataLoader.movieType
            ContentType.TvShow -> ApiDataLoader.tvType
        }
    }
}