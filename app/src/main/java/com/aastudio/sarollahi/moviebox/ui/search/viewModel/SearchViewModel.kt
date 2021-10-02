/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.search.viewModel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.api.response.GetTVShowResponse
import com.aastudio.sarollahi.common.CombinedLiveData
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.MOVIE_SEARCH_ERROR
import com.aastudio.sarollahi.common.tracker.TV_SEARCH_ERROR
import retrofit2.Call

class SearchViewModel(application: Application) : ViewModel() {

    private val context = application
    val moviesList = MutableLiveData<ArrayList<Movie>>()
    val showsList = MutableLiveData<ArrayList<TVShow>>()

    var loading = MutableLiveData<Boolean>()

    val result = CombinedLiveData(moviesList, showsList) { movies, shows ->
        hashMapOf(Pair("movies", movies), Pair("shows", shows))
    }

    fun searchMovie(page: Int, title: String, year: String) {
        Repository.searchMovies(
            page,
            title,
            year,
            ::onMoviesFetched,
            ::onMovieError
        )
    }

    fun searchTV(page: Int, title: String, year: String) {
        Repository.searchTVShows(
            page,
            title,
            year,
            ::onTVFetched,
            ::onTVError
        )
    }

    fun findMoviesByGenre(page: Int, sort: String, id: Int) {
        Repository.findMoviesByGenre(
            page,
            sort,
            id,
            ::onMoviesFetched,
            ::onMovieError
        )
    }

    fun findTVShowsByGenre(page: Int, sort: String, id: Int) {
        Repository.findTVByGenre(
            page,
            sort,
            id,
            ::onTVFetched,
            ::onTVError
        )
    }

    private fun onMoviesFetched(movies: List<Movie>) {
        moviesList.value = movies.toTypedArray().toCollection(ArrayList())
        loading.value = false
    }

    private fun onTVFetched(shows: List<TVShow>) {
        showsList.value = shows.toTypedArray().toCollection(ArrayList())
        loading.value = false
    }

    private fun onMovieError(call: Call<GetMoviesResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, MOVIE_SEARCH_ERROR, bundle)
    }

    private fun onTVError(call: Call<GetTVShowResponse>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TV_SEARCH_ERROR, bundle)
    }
}
