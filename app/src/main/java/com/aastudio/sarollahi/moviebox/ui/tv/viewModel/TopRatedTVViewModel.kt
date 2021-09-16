/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.TOP_RATED_TV_ERROR
import retrofit2.Call

class TopRatedTVViewModel(application: Application) : ViewModel() {

    private val context = application
    val topRatedList = MutableLiveData<List<TVShow>>()

    fun getTVShows(page: Int) {
        Repository.getTopRatedTVShows(
            page,
            "us",
            ::onTopRatedTVShowsFetched,
            ::onTVShowsError
        )
    }

    private fun onTopRatedTVShowsFetched(shows: List<TVShow>) {
        topRatedList.value = shows
    }

    private fun onTVShowsError(call: Call<GetTVShowResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TOP_RATED_TV_ERROR, bundle)
    }
}
