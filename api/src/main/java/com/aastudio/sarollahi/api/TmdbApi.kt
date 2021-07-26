/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api

import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    companion object {
        const val KEY = ""
    }

    // MOVIES
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String = KEY,
        @Query("append_to_response") append_to_response: String = "external_ids,credits,reviews,similar,recommendations"
    ): Call<Movie>

    @GET("genre/movie/list")
    fun getMovieGenres(
        @Query("api_key") apiKey: String = KEY
    ): Call<Movie>

    // TV SHOWS
    @GET("tv/popular")
    fun getPopularTVShows(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("tv/top_rated")
    fun getTopRatedTVShows(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("tv/on_the_air")
    fun getUpcomingTVShows(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("tv/airing_today")
    fun getNowPlayingTVShows(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("tv/{showId}")
    fun getTVShowDetails(
        @Path("showId") showId: Long,
        @Query("api_key") apiKey: String = KEY,
        @Query("append_to_response") append_to_response: String = "external_ids,credits,reviews,similar,recommendations,episode_groups"
    ): Call<TVShow>

    @GET("genre/tv/list")
    fun getTVGenres(
        @Query("api_key") apiKey: String = KEY,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    // PEOPLE
    @GET("person/{person_id}")
    fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = KEY,
        @Query("append_to_response") append_to_response: String = "external_ids,images"
    ): Call<Person>
}
