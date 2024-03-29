/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.utils.TvBackgroundManager
import com.aastudio.sarollahi.moviebox.views.activities.ErrorActivity
import com.aastudio.sarollahi.moviebox.views.activities.MainActivity
import com.aastudio.sarollahi.moviebox.views.activities.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.views.presenters.MovieViewPresenter
import retrofit2.Call

class MainFragment : BrowseSupportFragment() {
    private val tvBackgroundManager: TvBackgroundManager by lazy {
        TvBackgroundManager(activity as MainActivity).apply {
            clearBackground()
        }
    }
    private var nowPlayingMoviesPage = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        headersState = HEADERS_ENABLED
        showTitle(true)
        badgeDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.banner
        )
        brandColor = ContextCompat.getColor(
            requireContext(),
            R.color.headers_background
        )

        getNowPlayingMovies()

        onItemViewSelectedListener =
            OnItemViewSelectedListener { _, item, _, _ ->
                if (item is Movie) {
                    item.backdropPath?.let {
                        tvBackgroundManager.updateBackground("https://image.tmdb.org/t/p/original$it")
                    }
                } else {
                    tvBackgroundManager.clearBackground()
                }
            }

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Movie) {
                startActivity(
                    Intent(activity, MovieDetailsActivity::class.java).putExtra(
                        MovieDetailsFragment.EXTRA_MOVIE, item
                    )
                )
            } else {
                startActivity(Intent(requireContext(), ErrorActivity::class.java))
            }
        }
    }

    private fun getNowPlayingMovies() {
        Repository.getNowPlayingMovies(
            nowPlayingMoviesPage,
            "us",
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun onNowPlayingMoviesFetched(movies: List<Movie>) {
        val headerItem = HeaderItem(0, "Movies")
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val movieViewPresenter = MovieViewPresenter(requireContext())
        val movieRowAdapter = ArrayObjectAdapter(movieViewPresenter)
        for (movie in movies) {
            movieRowAdapter.add(movie)
        }
        rowsAdapter.add(ListRow(headerItem, movieRowAdapter))

        adapter = rowsAdapter
//        attachPopularMoviesOnScrollListener()
    }

//    private fun attachPopularMoviesOnScrollListener() {
//        nowPlayingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val totalItemCount = nowPlayingMoviesLayoutMgr.itemCount
//                val visibleItemCount = nowPlayingMoviesLayoutMgr.childCount
//                val firstVisibleItem = nowPlayingMoviesLayoutMgr.findFirstVisibleItemPosition()
//
//                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
//                    nowPlayingMovies.removeOnScrollListener(this)
//                    nowPlayingMoviesPage++
//                    getNowPlayingMovies()
//                }
//            }
//        })
//    }

    private fun onError(call: Call<GetMoviesResponse>, error: String) {
        Toast.makeText(
            requireContext(),
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}
