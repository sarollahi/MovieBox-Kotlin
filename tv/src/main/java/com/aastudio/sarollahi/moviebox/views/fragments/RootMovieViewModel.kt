/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.fragments

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.R

class RootMovieViewModel(private val application: Application) : ViewModel() {

    val nowPlayingList = MutableLiveData<List<Movie>>()

    fun getMovies() {
        MoviesRepository.getNowPlayingMovies(
            1,
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun onNowPlayingMoviesFetched(playingMovies: List<Movie>) {
        nowPlayingList.value = playingMovies
    }

    private fun onError() {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }
}
