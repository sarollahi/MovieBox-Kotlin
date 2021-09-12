/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tvDetails

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.api.model.Language
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.moviebox.R
import retrofit2.Call

class TVViewModel(private val application: Application) : ViewModel() {

    private val tag = "TVViewModel"
    val show = MutableLiveData<TVShow>()
    var loading = MutableLiveData<Boolean>()
    val torrentTv = MutableLiveData<List<BaseMovie.Torrent>>()
    val languageList = MutableLiveData<List<Language>>()

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

        // Get Torrent
//        show.imdbId?.let {
//            Repository.torrent(
//                it,
//                ::onMovieTorrentsFetched,
//                ::onTorrentError
//            )
//        }
    }

    private fun onMovieTorrentsFetched(list: List<BaseMovie.Movie>?) {
        if (list != null) {
            for (torrentList in list) {
                torrentTv.value = torrentList.torrents
            }
        }
    }

    private fun onError(call: Call<TVShow>, error: String) {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onTorrentError() {
        Log.d(tag, "no play link!")
    }

    fun getGenres(genres: List<Genre?>?): String {
        var movieGenre = ""
        if (genres != null) {
            for (genre in genres) {
                movieGenre = if (movieGenre == "") {
                    StringBuilder(movieGenre).append(genre?.name).toString()
                } else {
                    StringBuilder(movieGenre).append(", ${genre?.name}").toString()
                }
            }
        }
        return movieGenre
    }

    fun refactorTime(time: Int): String {
        return if (time != 0) {
            val runtime: String
            val hours: Int = time / 60 // since both are ints, you get an int
            val minutes: Int = time % 60
            runtime = String.format("%d H %02d Min", hours, minutes)
            runtime
        } else {
            ""
        }
    }
}
