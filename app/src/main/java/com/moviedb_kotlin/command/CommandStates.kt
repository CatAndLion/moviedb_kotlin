package com.moviedb_kotlin.command

import android.text.TextUtils


open class BaseState(val running: Boolean) {
    var errorMessage: String? = null

    val withError: Boolean
        get() = !TextUtils.isEmpty(errorMessage)

    constructor(error: String?) : this(false) {
        errorMessage = error
    }
}

open class DataState<D>(val data: D): BaseState(false)

open class ListState<D>(data: List<D>, val total: Int): DataState<List<D>>(data)