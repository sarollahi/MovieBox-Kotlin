/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.POPULAR_ADS_PLACEMENT_ID
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivitySearchBinding
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), NativeAdsManager.Listener {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private var nativeAdsManager: NativeAdsManager? = null
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesLayoutMgr: LinearLayoutManager
    private var genreId: Int? = null
    private var page = 1
    private var sort = "popularity.desc"

    companion object {
        const val GENRE_ID = "extra_genre_id"
        const val GENRE_NAME = "extra_genre_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(GENRE_NAME) ?: getString(
            R.string.app_name
        )

        nativeAdsManager = NativeAdsManager(this, POPULAR_ADS_PLACEMENT_ID, 5)
        nativeAdsManager?.loadAds()
        nativeAdsManager?.setListener(this)

        moviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                true,
                this,
                nativeAdsManager
            ) { movie -> showMovieDetails(movie) }
        moviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.searchContent.searchContainer.layoutManager = moviesLayoutMgr

        genreId = intent.getIntExtra(GENRE_ID, -1)
        if (genreId != null && genreId != -1) {
            viewModel.findMovies(page, sort, genreId!!)
        } else {
            finish()
        }

        viewModel.apply {
            observe(moviesList) {
                moviesAdapter.appendMovies(it)
                attachPopularMoviesOnScrollListener()
            }
        }

        binding.search.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun attachPopularMoviesOnScrollListener() {
        binding.searchContent.searchContainer.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val totalItemCount = moviesLayoutMgr.itemCount
                    val visibleItemCount = moviesLayoutMgr.childCount
                    val firstVisibleItem = moviesLayoutMgr.findFirstVisibleItemPosition()

                    if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                        binding.searchContent.searchContainer.removeOnScrollListener(this)
                        page++
                        genreId?.let { id -> viewModel.findMovies(page, sort, id) }
                    }
                }
            })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }

    override fun onAdsLoaded() {
        moviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                true,
                this,
                nativeAdsManager
            ) { movie -> showMovieDetails(movie) }
        binding.searchContent.searchContainer.adapter = moviesAdapter

        genreId?.let { id -> viewModel.findMovies(page, sort, id) }
    }

    override fun onAdError(error: AdError) {
        moviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                false,
                null,
                null
            ) { movie -> showMovieDetails(movie) }
        binding.searchContent.searchContainer.adapter = moviesAdapter

        genreId?.let { id -> viewModel.findMovies(page, sort, id) }
    }
}
