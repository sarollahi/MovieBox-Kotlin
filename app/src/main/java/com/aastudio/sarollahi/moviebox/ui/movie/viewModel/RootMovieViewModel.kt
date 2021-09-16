/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movie.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.MOVIE_GENRES_ERROR
import com.aastudio.sarollahi.common.tracker.ROOT_MOVIE_ERROR
import retrofit2.Call

class RootMovieViewModel(application: Application) : ViewModel() {

    private val context = application
    val upcomingList = MutableLiveData<List<Movie>>()
    val nowPlayingList = MutableLiveData<List<Movie>>()
    val popularList = MutableLiveData<List<Movie>>()
    val topRatedList = MutableLiveData<List<Movie>>()
    val movieGenres = MutableLiveData<List<Genre>>()

    fun getMovies() {
        val it = "us"
        Repository.getUpcomingMovies(
            1,
            it,
            ::onUpcomingMoviesFetched,
            ::onMovieError
        )
        Repository.getNowPlayingMovies(
            1,
            it,
            ::onNowPlayingMoviesFetched,
            ::onMovieError
        )
        Repository.getPopularMovies(
            1,
            it,
            ::onPopularMoviesFetched,
            ::onMovieError
        )
        Repository.getTopRatedMovies(
            1,
            it,
            ::onTopRatedMoviesFetched,
            ::onMovieError
        )
    }

    fun getGenres() {
        Repository.getMovieGenres(
            ::onMovieGenresFetched,
            ::onGenreError
        )
    }

    private fun onUpcomingMoviesFetched(upcomingMovies: List<Movie>) {
        upcomingList.value = upcomingMovies
    }

    private fun onNowPlayingMoviesFetched(playingMovies: List<Movie>) {
        nowPlayingList.value = playingMovies
    }

    private fun onPopularMoviesFetched(popularMovies: List<Movie>) {
        popularList.value = popularMovies
    }

    private fun onTopRatedMoviesFetched(topRatedMovies: List<Movie>) {
        topRatedList.value = topRatedMovies
    }

    private fun onMovieGenresFetched(genres: List<Genre>) {
        movieGenres.value = genres
    }

    private fun onGenreError(call: Call<Movie>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, MOVIE_GENRES_ERROR, bundle)
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, ROOT_MOVIE_ERROR, bundle)
    }
}
