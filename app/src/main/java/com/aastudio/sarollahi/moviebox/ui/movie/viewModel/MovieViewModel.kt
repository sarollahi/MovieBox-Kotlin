/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movie.viewModel

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.common.logEvent
import com.aastudio.sarollahi.common.tracker.MOVIE_DETAILS_ERROR
import com.aastudio.sarollahi.common.tracker.TORRENT_ERROR
import com.aastudio.sarollahi.moviebox.R
import retrofit2.Call

class MovieViewModel(application: Application) : ViewModel() {

    private val context = application
    val movie = MutableLiveData<Movie>()
    var loading = MutableLiveData<Boolean>()
    val torrentTv = MutableLiveData<List<BaseMovie.Torrent>>()

    fun getMovieDetails(movieId: Long) {
        Repository.getMovieDetails(
            movieId,
            ::onMovieDetailsFetched,
            ::onError
        )
    }

    private fun onMovieDetailsFetched(
        movie: Movie
    ) {
        this.movie.value = movie
        loading.value = false

        // Get Torrent
        movie.imdbId?.let {
            Repository.torrent(
                it,
                ::onMovieTorrentsFetched,
                ::onTorrentError
            )
        }
    }

    private fun onMovieTorrentsFetched(list: List<BaseMovie.Movie>?) {
        list?.let {
            for (torrentList in it) {
                torrentList.torrents?.let { torrents ->
                    torrentTv.value = torrents
                }
            }
        }
    }

    private fun onError(call: Call<Movie>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, MOVIE_DETAILS_ERROR, bundle)
    }

    private fun onTorrentError(call: Call<BaseMovie>, error: String) {
        val bundle = Bundle()
        bundle.putString("Call", call.toString())
        bundle.putString("Error", error)
        logEvent(context, TORRENT_ERROR, bundle)
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
