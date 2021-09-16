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
import com.aastudio.sarollahi.common.tracker.POPULAR_MOVIE_ERROR
import retrofit2.Call

class PopularMoviesViewModel(application: Application) : ViewModel() {

    private val context = application
    val popularList = MutableLiveData<List<Movie>>()

    fun getMovies(page: Int) {
        Repository.getPopularMovies(
            page,
            "us",
            ::onPopularMoviesFetched,
            ::onMovieError
        )
    }

    private fun onPopularMoviesFetched(playingMovies: List<Movie>) {
        popularList.value = playingMovies
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, POPULAR_MOVIE_ERROR, bundle)
    }
}
