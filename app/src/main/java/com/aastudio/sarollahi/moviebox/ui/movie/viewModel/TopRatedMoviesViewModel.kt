/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movie.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.TOP_RATED_MOVIE_ERROR
import retrofit2.Call

class TopRatedMoviesViewModel(application: Application) : ViewModel() {

    private val context = application
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
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TOP_RATED_MOVIE_ERROR, bundle)
    }
}
