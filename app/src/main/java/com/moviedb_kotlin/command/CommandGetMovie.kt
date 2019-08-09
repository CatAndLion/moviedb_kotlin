package com.moviedb_kotlin.command

import com.moviedb_kotlin.loaders.ApiDataLoader
import com.moviedb_kotlin.viewmodels.ContentFull
import io.reactivex.Observable


class CommandGetMovie(val id: Int): Command<ContentFull>() {

    override fun getCall(): Observable<ContentFull> {

        return ApiDataLoader().getMovie(id)
            .map {
                ContentFull.fromProtocol(it)
            }.toObservable()
    }

    override fun getCompleteState(response: ContentFull): BaseState {
        return DataState(response)
    }

}