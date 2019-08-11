package com.moviedb_kotlin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviedb_kotlin.command.CommandExecutor
import com.moviedb_kotlin.command.GetTrendingCommand
import com.moviedb_kotlin.command.ListState
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class ContentListViewModel : ViewModel() {

    var contentType: ContentType = ContentType.Movie

    var total: Int = 0

    var query: String? = null

    var isLoading = MutableLiveData<Boolean>()

    var currentError = MutableLiveData<String?>()

    val data: MutableList<Content> = mutableListOf()
    val newData = PublishSubject.create<List<Content>>()

    var disposable: Disposable? = null

    val hasPagesToLoad: Boolean
        get() = total > data.size

    init {
        isLoading.value = false
    }

    fun loadData(offset: Int = 0, query: String? = null) {
        if(disposable?.isDisposed == false) {
            disposable?.dispose()
        }

        this.query = query
        if(offset == 0) {
            this.total = 0
            this.data.clear()
        }

        disposable = CommandExecutor.execute(GetTrendingCommand(10, offset, contentType, query)) { state ->

                isLoading.value = state.running
                currentError.value = state.errorMessage
                if(state is ListState<*>) {
                    total = state.total
                    data.addAll(state.data as List<Content>)
                    newData.onNext(state.data as List<Content>)
                }
            }
    }
}