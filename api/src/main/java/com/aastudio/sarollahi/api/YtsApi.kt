package com.aastudio.sarollahi.api

import com.aastudio.sarollahi.api.model.BaseMovie
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface YtsApi {

    @GET("list_movies.json?")
    fun getMoviesList(
        @Query("query_term") imdbId: String
    ): Call<BaseMovie>

    @GET
    fun downloadFile(@Url fileUrl: String): Observable<Response<ResponseBody>>
}