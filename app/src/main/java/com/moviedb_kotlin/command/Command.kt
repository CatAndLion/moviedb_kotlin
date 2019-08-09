package com.moviedb_kotlin.command

import io.reactivex.Observable
import io.reactivex.Single

abstract class Command<R> {

    abstract fun getCall(): Observable<R>

    abstract fun getCompleteState(response: R): BaseState

}