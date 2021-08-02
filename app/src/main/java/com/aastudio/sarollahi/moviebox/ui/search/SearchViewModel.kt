/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.search

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.R

class SearchViewModel(private val application: Application) : ViewModel() {

    val moviesList = MutableLiveData<List<Movie>>()

    fun findMovies(page: Int, sort: String, id: Int) {
        MoviesRepository.findMoviesByGenre(
            page,
            sort,
            id,
            ::onMoviesFetched,
            ::onError
        )
    }

    private fun onMoviesFetched(movies: List<Movie>) {
        moviesList.value = movies
    }

    private fun onError() {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }
}
