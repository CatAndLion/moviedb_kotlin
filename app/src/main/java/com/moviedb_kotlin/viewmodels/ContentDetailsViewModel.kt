package com.moviedb_kotlin.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviedb_kotlin.command.*

class ContentDetailsViewModel: ViewModel() {

    var contentType: ContentType = ContentType.Movie

    var isLoading = MutableLiveData<Boolean>()

    var content = MutableLiveData<ContentFull>()

    fun loadData(id: Int) {

        val command = when(contentType) {
            ContentType.Movie -> GetMovieCommand(id)
            ContentType.TvShow -> GetTvShowCommand(id)
        }

        val d = CommandExecutor.execute(command) { state ->
            isLoading.value = state.running
            if(state is DataState<*>) {
                content.value = state.data as ContentFull

                // get cast list
                val id = content.value?.id ?: 0
                CommandExecutor.execute(GetCastCommand(id, contentType)) { state ->
                    if(state is ListState<*>) {
                        content.value?.cast = state.data as List<Person>
                        content.postValue(content.value)
                    }
                }
            }
        }
    }
}