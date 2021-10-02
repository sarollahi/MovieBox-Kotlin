/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.PersonMovies
import com.aastudio.sarollahi.api.model.PersonTVShows
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.common.CombinedLiveData
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.PERSON_CREDITS_ERROR
import retrofit2.Call

class CreditsViewModel(application: Application) : ViewModel() {
    private val context = application
    val movies = MutableLiveData<List<Movie>>()
    val shows = MutableLiveData<List<TVShow>>()

    val creditsList = CombinedLiveData(shows, movies) { shows, movies ->
        hashMapOf(Pair("movie", movies), Pair("show", shows))
    }

    fun getPersonCredits(personId: Int) {
        Repository.getPersonMovies(
            personId,
            ::onPersonMoviesFetched,
            ::onMovieError
        )
        Repository.getPersonTVs(
            personId,
            ::onPersonTVsFetched,
            ::onTVError
        )
    }

    private fun onPersonMoviesFetched(movies: List<Movie>) {
        this.movies.value = movies
    }

    private fun onPersonTVsFetched(shows: List<TVShow>) {
        this.shows.value = shows
    }

    private fun onMovieError(call: Call<PersonMovies?>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, PERSON_CREDITS_ERROR, bundle)
    }

    private fun onTVError(call: Call<PersonTVShows?>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, PERSON_CREDITS_ERROR, bundle)
    }
}
