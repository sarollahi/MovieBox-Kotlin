/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.rootMovie

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.R

class RootMovieViewModel(private val application: Application) : ViewModel() {

    val upcomingList = MutableLiveData<List<Movie>>()
    val nowPlayingList = MutableLiveData<List<Movie>>()
    val popularList = MutableLiveData<List<Movie>>()
    val topRatedList = MutableLiveData<List<Movie>>()
    val movieGenres = MutableLiveData<List<Genre>>()

    fun getMovies() {
        MoviesRepository.getUpcomingMovies(
            1,
            ::onUpcomingMoviesFetched,
            ::onError
        )
        MoviesRepository.getNowPlayingMovies(
            1,
            ::onNowPlayingMoviesFetched,
            ::onError
        )
        MoviesRepository.getPopularMovies(
            1,
            ::onPopularMoviesFetched,
            ::onError
        )
        MoviesRepository.getTopRatedMovies(
            1,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    fun getGenres() {
        MoviesRepository.getMovieGenres(
            ::onMovieGenresFetched,
            ::onError
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

    private fun onError() {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }
}
