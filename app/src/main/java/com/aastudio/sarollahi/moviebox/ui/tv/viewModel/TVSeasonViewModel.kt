/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Episode
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.TV_SEASON_ERROR
import retrofit2.Call

class TVSeasonViewModel(application: Application) : ViewModel() {

    private val context = application
    val episodesList = MutableLiveData<List<Episode>?>()

    fun getEpisodes(showId: Long, season: Int) {
        Repository.getEpisodes(
            showId,
            season,
            ::onEpisodesFetched,
            ::onError
        )
    }

    private fun onEpisodesFetched(episodes: List<Episode>?) {
        episodesList.value = episodes
    }

    private fun onError(call: Call<TVShow>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TV_SEASON_ERROR, bundle)
    }
}
