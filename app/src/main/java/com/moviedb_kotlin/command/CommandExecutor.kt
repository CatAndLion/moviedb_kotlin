package com.moviedb_kotlin.command

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object CommandExecutor {

    fun <C> execute(command: Command<C>, consumer: (state: BaseState) -> Unit): Disposable {
        return command.getCall()
            .subscribeOn(Schedulers.io())
            .map {
                command.getCompleteState(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(BaseState(true))
            .onErrorReturn {
                BaseState(it.message)
            }
            .subscribe(consumer)
    }

}