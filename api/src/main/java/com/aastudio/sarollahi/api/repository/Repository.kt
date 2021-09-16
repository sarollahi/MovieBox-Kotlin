/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.api.repository

import com.aastudio.sarollahi.api.TmdbApi
import com.aastudio.sarollahi.api.YtsApi
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.api.model.Episode
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KFunction2

object Repository {
    private val TMDB_API: TmdbApi
    private val YTS_API: YtsApi

    init {
        val tmdbRetrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ytsRetrofit = Retrofit.Builder()
            .baseUrl("https://yts.am/api/v2/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        TMDB_API = tmdbRetrofit.create(TmdbApi::class.java)
        YTS_API = ytsRetrofit.create(YtsApi::class.java)
    }

    fun getPopularMovies(
        page: Int = 1,
        region: String = "us",
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: (call: Call<GetMoviesResponse>, error: String) -> Unit
    ) {
        TMDB_API.getPopularMovies(page = page, region = region)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getTopRatedMovies(
        page: Int = 1,
        region: String = "us",
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: (call: Call<GetMoviesResponse>, error: String) -> Unit
    ) {
        TMDB_API.getTopRatedMovies(page = page, region = region)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getUpcomingMovies(
        page: Int = 1,
        region: String = "us",
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: (call: Call<GetMoviesResponse>, error: String) -> Unit
    ) {
        TMDB_API.getUpcomingMovies(page = page, region = region)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getNowPlayingMovies(
        page: Int = 1,
        region: String = "us",
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: (call: Call<GetMoviesResponse>, error: String) -> Unit
    ) {
        TMDB_API.getNowPlayingMovies(page = page, region = region)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun findMoviesByGenre(
        page: Int = 1,
        sort: String,
        id: Int,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: (call: Call<GetMoviesResponse>, error: String) -> Unit
    ) {
        TMDB_API.findMoviesByGenre(page = page, sortBy = sort, genreId = id)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getMovieDetails(
        id: Long,
        onSuccess: (movies: Movie) -> Unit,
        onError: (call: Call<Movie>, error: String) -> Unit
    ) {
        TMDB_API.getMovieDetails(movieId = id)
            .enqueue(object : Callback<Movie> {
                override fun onResponse(
                    call: Call<Movie>,
                    response: Response<Movie>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getMovieGenres(
        onSuccess: (genre: List<Genre>) -> Unit,
        onError: (call: Call<Movie>, error: String) -> Unit
    ) {
        TMDB_API.getMovieGenres()
            .enqueue(object : Callback<Movie> {
                override fun onResponse(
                    call: Call<Movie>,
                    response: Response<Movie>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.genre
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun torrent(
        imdbId: String,
        onSuccess: (movies: List<BaseMovie.Movie>?) -> Unit,
        onError: (call: Call<BaseMovie>, error: String) -> Unit
    ) {
        YTS_API.getMoviesList(imdbId)
            .enqueue(object : Callback<BaseMovie> {
                override fun onResponse(
                    call: Call<BaseMovie>,
                    response: Response<BaseMovie>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.data?.movies
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<BaseMovie>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getPersonDetails(
        id: Int,
        onSuccess: (person: Person) -> Unit,
        onError: (call: Call<Person?>, error: String) -> Unit
    ) {
        TMDB_API.getPersonDetails(personId = id)
            .enqueue(object : Callback<Person?> {
                override fun onResponse(
                    call: Call<Person?>,
                    response: Response<Person?>
                ) {
                    if (response.isSuccessful) {
                        val person: Person? = response.body()
                        if (person != null) {
                            onSuccess.invoke(person)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Person?>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getPopularTVShows(
        page: Int = 1,
        region: String = "us",
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: (call: Call<GetTVShowResponse>, error: String) -> Unit
    ) {
        TMDB_API.getPopularTVShows(page = page, region = region)
            .enqueue(object : Callback<GetTVShowResponse> {
                override fun onResponse(
                    call: Call<GetTVShowResponse>,
                    response: Response<GetTVShowResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.tvShows)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getTopRatedTVShows(
        page: Int = 1,
        region: String = "us",
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: (call: Call<GetTVShowResponse>, error: String) -> Unit
    ) {
        TMDB_API.getTopRatedTVShows(page = page, region = region)
            .enqueue(object : Callback<GetTVShowResponse> {
                override fun onResponse(
                    call: Call<GetTVShowResponse>,
                    response: Response<GetTVShowResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.tvShows)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getUpcomingTVShows(
        page: Int = 1,
        region: String = "us",
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: (call: Call<GetTVShowResponse>, error: String) -> Unit
    ) {
        TMDB_API.getUpcomingTVShows(page = page, region = region)
            .enqueue(object : Callback<GetTVShowResponse> {
                override fun onResponse(
                    call: Call<GetTVShowResponse>,
                    response: Response<GetTVShowResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.tvShows)
                        } else {
                            onError.invoke(call, "response.errorBody().toString()")
                        }
                    } else {
                        onError.invoke(call, "response.errorBody().toString()")
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTraceToString())
                }
            })
    }

    fun getNowPlayingTVShows(
        page: Int = 1,
        region: String = "us",
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: (call: Call<GetTVShowResponse>, error: String) -> Unit
    ) {
        TMDB_API.getNowPlayingTVShows(page = page, region = region)
            .enqueue(object : Callback<GetTVShowResponse> {
                override fun onResponse(
                    call: Call<GetTVShowResponse>,
                    response: Response<GetTVShowResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.tvShows)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getTVShowDetails(
        id: Long,
        onSuccess: (show: TVShow) -> Unit,
        onError: KFunction2<Call<TVShow>, String, Unit>
    ) {
        TMDB_API.getTVShowDetails(showId = id)
            .enqueue(object : Callback<TVShow> {
                override fun onResponse(
                    call: Call<TVShow>,
                    response: Response<TVShow>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<TVShow>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun getEpisodes(
        id: Long,
        season: Int,
        onSuccess: (episodes: List<Episode>?) -> Unit,
        onError: KFunction2<Call<TVShow>, String, Unit>
    ) {
        TMDB_API.getEpisodes(showId = id, seasonNumber = season)
            .enqueue(object : Callback<TVShow> {
                override fun onResponse(
                    call: Call<TVShow>,
                    response: Response<TVShow>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.episodes)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<TVShow>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }

    fun findTVByGenre(
        page: Int = 1,
        sort: String,
        id: Int,
        onSuccess: (shows: List<TVShow>) -> Unit,
        onError: (call: Call<GetTVShowResponse>, error: String) -> Unit
    ) {
        TMDB_API.findTVByGenre(page = page, sortBy = sort, genreId = id)
            .enqueue(object : Callback<GetTVShowResponse> {
                override fun onResponse(
                    call: Call<GetTVShowResponse>,
                    response: Response<GetTVShowResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.tvShows)
                        } else {
                            onError.invoke(call, response.errorBody().toString())
                        }
                    } else {
                        onError.invoke(call, response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke(call, t.stackTrace.toString())
                }
            })
    }
}
