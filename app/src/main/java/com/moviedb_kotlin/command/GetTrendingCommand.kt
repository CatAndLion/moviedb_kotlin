package com.moviedb_kotlin.command

import com.moviedb_kotlin.loaders.ApiDataLoader
import com.moviedb_kotlin.loaders.PagedData
import com.moviedb_kotlin.viewmodels.Content
import com.moviedb_kotlin.viewmodels.ContentType
import io.reactivex.Observable

class GetTrendingCommand(val limit: Int,
                         val offset: Int,
                         val type: ContentType,
                         val query: String?): Command<PagedData<Content>>() {

    val loader = ApiDataLoader()

    override fun getCall(): Observable<PagedData<Content>> {
        return loader.getContentOfType(limit, offset, toProtocolType(type), query)
            .map { response ->
                PagedData(response.total, response.data.map { Content.fromProtocol(it, type) })
            }.toObservable()
    }

    override fun getCompleteState(response: PagedData<Content>): BaseState {
        return ListState(response.data, response.total)
    }

    val toProtocolType: (type: ContentType) -> String = {
        when(it) {
            ContentType.Movie -> ApiDataLoader.movieType
            ContentType.TvShow -> ApiDataLoader.tvType
        }
    }
}