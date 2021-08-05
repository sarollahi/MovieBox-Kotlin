/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.rootMovie

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import retrofit2.Call

class RootMovieViewModel(private val application: Application) : ViewModel() {

    val upcomingList = MutableLiveData<List<Movie>>()
    val nowPlayingList = MutableLiveData<List<Movie>>()
    val popularList = MutableLiveData<List<Movie>>()
    val topRatedList = MutableLiveData<List<Movie>>()
    val movieGenres = MutableLiveData<List<Genre>>()

    fun getMovies(context: Context) {
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
        Toast.makeText(
            application.applicationContext,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        Toast.makeText(
            application.applicationContext,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}
