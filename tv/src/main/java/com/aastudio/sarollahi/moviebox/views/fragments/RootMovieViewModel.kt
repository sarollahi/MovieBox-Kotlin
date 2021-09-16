/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.fragments

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import retrofit2.Call

class RootMovieViewModel(private val application: Application) : ViewModel() {

    val nowPlayingList = MutableLiveData<List<Movie>>()

    fun getMovies() {
        Repository.getNowPlayingMovies(
            1,
            "us",
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun onNowPlayingMoviesFetched(playingMovies: List<Movie>) {
        nowPlayingList.value = playingMovies
    }

    private fun onError(call: Call<GetMoviesResponse>, error: String) {
    }
}
