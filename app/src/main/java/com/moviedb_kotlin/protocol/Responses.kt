package com.moviedb_kotlin.protocol

import com.google.gson.annotations.SerializedName

class ListResponse<T>(val page: Int,
                      @SerializedName("total_results")
                      val total: Int,
                      @SerializedName("total_pages")
                      val count: Int,
                      @SerializedName("results")
                      val data: Array<T>)

class CastResponse(val cast: Array<Person>)