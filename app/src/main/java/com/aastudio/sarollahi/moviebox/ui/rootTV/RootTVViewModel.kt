/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.rootTV

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import retrofit2.Call

class RootTVViewModel(private val application: Application) : ViewModel() {

    val upcomingList = MutableLiveData<List<TVShow>>()
    val nowPlayingList = MutableLiveData<List<TVShow>>()
    val popularList = MutableLiveData<List<TVShow>>()
    val topRatedList = MutableLiveData<List<TVShow>>()
    val movieGenres = MutableLiveData<List<Genre>>()

    fun getShows(context: Context) {
        val region = "us"
        Repository.getUpcomingTVShows(
            1,
            region,
            ::onUpcomingShowsFetched,
            ::onShowError
        )
        Repository.getNowPlayingTVShows(
            1,
            region,
            ::onNowPlayingShowsFetched,
            ::onShowError
        )
        Repository.getPopularTVShows(
            1,
            region,
            ::onPopularShowsFetched,
            ::onShowError
        )
        Repository.getTopRatedTVShows(
            1,
            region,
            ::onTopRatedShowsFetched,
            ::onShowError
        )
    }

    fun getGenres() {
        Repository.getMovieGenres(
            ::onTVGenresFetched,
            ::onGenreError
        )
    }

    private fun onUpcomingShowsFetched(upcomingShows: List<TVShow>) {
        upcomingList.value = upcomingShows
    }

    private fun onNowPlayingShowsFetched(playingShows: List<TVShow>) {
        nowPlayingList.value = playingShows
    }

    private fun onPopularShowsFetched(popularShows: List<TVShow>) {
        popularList.value = popularShows
    }

    private fun onTopRatedShowsFetched(topRatedShows: List<TVShow>) {
        topRatedList.value = topRatedShows
    }

    private fun onTVGenresFetched(genres: List<Genre>) {
        movieGenres.value = genres
    }

    private fun onGenreError(call: Call<Movie>, error: String) {
        Toast.makeText(
            application.applicationContext,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onShowError(call: Call<GetTVShowResponse>, error: Throwable?) {
//        Toast.makeText(
//            application.applicationContext,
//            error,
//            Toast.LENGTH_SHORT
//        ).show()
    }

    private fun onShowError(call: Call<GetTVShowResponse>, error: String) {
        Toast.makeText(
            application.applicationContext,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}
