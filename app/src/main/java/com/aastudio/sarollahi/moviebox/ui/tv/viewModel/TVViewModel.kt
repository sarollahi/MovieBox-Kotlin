/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv.viewModel

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.TV_DETAILS_ERROR
import com.aastudio.sarollahi.moviebox.R
import retrofit2.Call

class TVViewModel(application: Application) : ViewModel() {

    private val context = application
    val show = MutableLiveData<TVShow>()
    var loading = MutableLiveData<Boolean>()
    val torrentTv = MutableLiveData<List<BaseMovie.Torrent>>()

    fun getShowDetails(movieId: Long) {
        Repository.getTVShowDetails(
            movieId,
            ::onShowDetailsFetched,
            ::onError
        )
    }

    private fun onShowDetailsFetched(
        show: TVShow
    ) {
        this.show.value = show
        loading.value = false
    }

    private fun onError(call: Call<TVShow>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TV_DETAILS_ERROR, bundle)
    }

    fun refactorTime(context: Context, time: Int): String {
        return if (time != 0) {
            val runtime: String
            val hours: Int = time / 60 // since both are ints, you get an int
            val minutes: Int = time % 60
            runtime = if (hours == 0) {
                context.getString(R.string.runtime_min, minutes)
            } else {
                context.getString(R.string.runtime_hour_min, hours, minutes)
            }
            runtime
        } else {
            ""
        }
    }
}
