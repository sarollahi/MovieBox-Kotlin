/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.topRatedMovies

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import retrofit2.Call

class TopRatedMoviesViewModel(private val application: Application) : ViewModel() {

    val topRatedList = MutableLiveData<List<Movie>>()

    fun getMovies(page: Int) {
        Repository.getTopRatedMovies(
            page,
            "us",
            ::onTopRatedMoviesFetched,
            ::onMovieError
        )
    }

    private fun onTopRatedMoviesFetched(playingMovies: List<Movie>) {
        topRatedList.value = playingMovies
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        Toast.makeText(
            application.applicationContext,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}
