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
import com.aastudio.sarollahi.common.tracker.NOW_PLAYING_ERROR
import retrofit2.Call

class NowPlayingMoviesViewModel(application: Application) : ViewModel() {

    private val context = application
    val nowPlayingList = MutableLiveData<List<Movie>>()

    fun getMovies(page: Int) {
        Repository.getNowPlayingMovies(
            page,
            "us",
            ::onNowPlayingMoviesFetched,
            ::onMovieError
        )
    }

    private fun onNowPlayingMoviesFetched(playingMovies: List<Movie>) {
        nowPlayingList.value = playingMovies
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, NOW_PLAYING_ERROR, bundle)
    }
}
