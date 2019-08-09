package com.moviedb_kotlin.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviedb_kotlin.command.*
import io.reactivex.disposables.Disposable

class ContentDetailsViewModel: ViewModel() {

    var contentType: ContentType = ContentType.Movie

    var isLoading = MutableLiveData<Boolean>()

    var content = MutableLiveData<ContentFull>()

    fun loadData(id: Int): Disposable {

        val command = when(contentType) {
            ContentType.Movie -> CommandGetMovie(id)
            ContentType.TvShow -> CommandGetTvShow(id)
        }

        return CommandExecutor.with(command).execute { state ->
            isLoading.value = state.running
            if(state is DataState<*>) {
                content.value = state.data as ContentFull

                // get cast list
                val id = content.value?.id ?: 0
                CommandExecutor.with(GetCastCommand(id, contentType)).execute { state ->
                    if(state is ListState<*>) {
                        content.value?.cast = state.data as List<Person>
                        content.postValue(content.value)
                    }
                }
            }
        }
    }
}