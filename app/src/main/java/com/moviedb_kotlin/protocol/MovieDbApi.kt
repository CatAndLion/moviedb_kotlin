package com.moviedb_kotlin.protocol

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MovieDbApi {

    const val apiKey = "befc7872520fd736c58948abb2f4a53c"

    const val url = "http://api.themoviedb.org/"

    const val imageUrl = "https://image.tmdb.org/t/p/w500/"

    val api : MovieDbRepository by lazy { create() }

    private fun create() : MovieDbRepository {

        val client = OkHttpClient.Builder().addInterceptor { chain ->

            println("Retrofit call ${chain.request().url()}")

            chain.proceed(chain.request())
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(MovieDbRepository::class.java)
    }
}