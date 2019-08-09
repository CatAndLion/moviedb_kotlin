package com.moviedb_kotlin.loaders

import android.text.TextUtils
import com.moviedb_kotlin.database.Content
import io.reactivex.Single

class CacheDataLoader {

    private val apiLoader = ApiDataLoader()
    private val dbLoader = DbLoader()

}