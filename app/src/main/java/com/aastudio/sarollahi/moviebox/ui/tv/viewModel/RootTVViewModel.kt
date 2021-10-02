/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.ROOT_TV_ERROR
import com.aastudio.sarollahi.common.tracker.TV_GENRES_ERROR
import retrofit2.Call

class RootTVViewModel(application: Application) : ViewModel() {

    private val context = application
    val upcomingList = MutableLiveData<List<TVShow>>()
    val nowPlayingList = MutableLiveData<List<TVShow>>()
    val popularList = MutableLiveData<List<TVShow>>()
    val topRatedList = MutableLiveData<List<TVShow>>()
    val movieGenres = MutableLiveData<List<Genre>>()

    fun getShows() {
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
        Repository.getTVGenres(
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

    private fun onGenreError(call: Call<TVShow>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TV_GENRES_ERROR, bundle)
    }

    private fun onShowError(call: Call<GetTVShowResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, ROOT_TV_ERROR, bundle)
    }
}
