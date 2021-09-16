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
import com.aastudio.sarollahi.common.tracker.UPCOMING_MOVIE_ERROR
import retrofit2.Call

class UpcomingMoviesViewModel(application: Application) : ViewModel() {

    private val context = application
    val upcomingList = MutableLiveData<List<Movie>>()

    fun getMovies(page: Int) {
        Repository.getUpcomingMovies(
            page,
            "us",
            ::onUpcomingMoviesFetched,
            ::onMovieError
        )
    }

    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingList.value = movies
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, UPCOMING_MOVIE_ERROR, bundle)
    }
}
