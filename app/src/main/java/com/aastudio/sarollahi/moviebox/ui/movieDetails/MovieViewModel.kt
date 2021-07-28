/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movieDetails

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Language
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.R

class MovieViewModel(private val application: Application) : ViewModel() {

    companion object {
    }

    val movie = MutableLiveData<Movie>()
    var loading = MutableLiveData<Boolean>()
    val torrentTv = MutableLiveData<List<BaseMovie.Torrent>>()
    val languageList = MutableLiveData<List<Language>>()

    fun getMovieDetails(movieId: Long) {
        MoviesRepository.getMovieDetails(
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
            MoviesRepository.torrent(
                it,
                ::onMovieTorrentsFetched,
                ::onTorrentError
            )
        }
    }

    private fun onMovieTorrentsFetched(list: List<BaseMovie.Movie>?) {
        if (list != null) {
            for (torrentList in list) {
                torrentTv.value = torrentList.torrents
            }
        }
    }

    private fun onError() {
        Toast.makeText(
            application.applicationContext,
            application.applicationContext.getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onTorrentError() {
        Toast.makeText(
            application.applicationContext,
            "torrent Error",
            Toast.LENGTH_SHORT
        ).show()
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
