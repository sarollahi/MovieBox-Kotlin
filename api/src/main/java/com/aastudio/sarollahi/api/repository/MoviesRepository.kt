package com.aastudio.sarollahi.api.repository

import com.aastudio.sarollahi.api.TmdbApi
import com.aastudio.sarollahi.api.YtsApi
import com.aastudio.sarollahi.api.model.*
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
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
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getPopularMovies(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTopRatedMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getTopRatedMovies(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getUpcomingMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getUpcomingMovies(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getNowPlayingMovies(
        page: Int = 1,
        onSuccess: (movies: List<Movie>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getNowPlayingMovies(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getMovieDetails(
        id: Long,
        onSuccess: (movies: Movie) -> Unit,
        onError: () -> Unit
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getMovieGenres(
        onSuccess: (genre: List<Genre>) -> Unit,
        onError: () -> Unit
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun torrent(
        imdbId: String,
        onSuccess: (movies: List<BaseMovie.Movie>?) -> Unit,
        onError: () -> Unit
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<BaseMovie>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getPersonDetails(
        id: Int,
        onSuccess: (person: Person) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getPersonDetails(personId = id)
            .enqueue(object : Callback<Person?> {
                override fun onResponse(
                    call: Call<Person?>,
                    response: Response<Person?>
                ) {
                    val a = response
                    if (response.isSuccessful) {
                        val person: Person? = response.body()
                        if (person != null) {
                            onSuccess.invoke(person)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<Person?>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getPopularTVShows(
        page: Int = 1,
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getPopularTVShows(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTopRatedTVShows(
        page: Int = 1,
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getTopRatedTVShows(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getUpcomingTVShows(
        page: Int = 1,
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getUpcomingTVShows(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getNowPlayingTVShows(
        page: Int = 1,
        onSuccess: (show: List<TVShow>) -> Unit,
        onError: () -> Unit
    ) {
        TMDB_API.getNowPlayingTVShows(page = page)
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<GetTVShowResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTVShowDetails(
        id: Long,
        onSuccess: (show: TVShow) -> Unit,
        onError: () -> Unit
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
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<TVShow>, t: Throwable) {
                    onError.invoke()
                }
            })
    }
}