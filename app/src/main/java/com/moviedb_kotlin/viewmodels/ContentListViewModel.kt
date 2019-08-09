package com.moviedb_kotlin.viewmodels

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

    var isLoading = MutableLiveData<Boolean>()

    var currentError = MutableLiveData<String?>()

    val data: MutableList<Content> = mutableListOf()
    val newData = PublishSubject.create<List<Content>>()

    val hasPagesToLoad: Boolean
        get() = total > data.size

    init {
        isLoading.value = false
    }

    fun loadData(offset: Int = 0): Disposable {

        return CommandExecutor.with(GetTrendingCommand(10, offset, contentType, ""))
            .execute { state ->
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