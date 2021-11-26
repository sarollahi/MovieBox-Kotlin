/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api

import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.model.PersonMovies
import com.aastudio.sarollahi.api.model.PersonTVShows
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.api.response.GetPersonResponse
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    // MOVIES
    @GET("search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("query") query: String,
        @Query("year") year: String,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetMoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetMoviesResponse>

    @GET("movie/{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("append_to_response") append_to_response: String = "external_ids,credits,reviews,similar,recommendations,videos"
    ): Call<Movie>

    @GET("discover/movie")
    fun findMoviesByGenre(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("sort_by") sortBy: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("genre/movie/list")
    fun getMovieGenres(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
    ): Call<Movie>

    // TV SHOWS
    @GET("search/tv")
    fun searchTVShow(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("query") query: String,
        @Query("first_air_date_year") year: String,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("tv/popular")
    fun getPopularTVShows(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetTVShowResponse>

    @GET("tv/top_rated")
    fun getTopRatedTVShows(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetTVShowResponse>

    @GET("tv/on_the_air")
    fun getUpcomingTVShows(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetTVShowResponse>

    @GET("tv/airing_today")
    fun getNowPlayingTVShows(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<GetTVShowResponse>

    @GET("tv/{showId}")
    fun getTVShowDetails(
        @Path("showId") showId: Long,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("append_to_response") append_to_response: String = "external_ids,credits,reviews,similar,recommendations"
    ): Call<TVShow>

    @GET("tv/{showId}/season/{seasonNumber}")
    fun getEpisodes(
        @Path("showId") showId: Long,
        @Path("seasonNumber") seasonNumber: Int,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
    ): Call<TVShow>

    @GET("discover/tv")
    fun findTVByGenre(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("sort_by") sortBy: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): Call<GetTVShowResponse>

    @GET("genre/tv/list")
    fun getTVGenres(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
    ): Call<TVShow>

    // PEOPLE
    @GET("search/person")
    fun searchPerson(
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<GetPersonResponse>

    @GET("person/{person_id}")
    fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
        @Query("append_to_response") append_to_response: String = "external_ids,images,movie_credits,tv_credits"
    ): Call<Person>

    @GET("person/{person_id}/movie_credits")
    fun getPersonMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
    ): Call<PersonMovies>

    @GET("person/{person_id}/tv_credits")
    fun getPersonTVs(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String? = System.getenv("API_KEY"),
    ): Call<PersonTVShows>
}
