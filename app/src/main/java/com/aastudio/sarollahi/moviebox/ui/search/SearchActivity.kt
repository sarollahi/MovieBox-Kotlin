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
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.TVShowsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivitySearchBinding
import com.aastudio.sarollahi.moviebox.ui.movie.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.tv.TVDetailsActivity
import com.facebook.ads.AudienceNetworkAds
import com.google.android.material.snackbar.Snackbar
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.nativeads.FacebookAdRenderer
import com.mopub.nativeads.MoPubRecyclerAdapter
import com.mopub.nativeads.MoPubStaticNativeAdRenderer
import com.mopub.nativeads.ViewBinder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var tvAdapter: TVShowsAdapter
    private lateinit var moviesLayoutMgr: LinearLayoutManager
    private var genreId: Int? = null
    private var page = 1
    private var sort = "popularity.desc"
    private var isMovie = true

    companion object {
        const val GENRE_ID = "extra_genre_id"
        const val GENRE_NAME = "extra_genre_name"
        const val IS_MOVIE = "extra_is_movie"
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

        AudienceNetworkAds.initialize(this)
        val sdkOnFiguration = SdkConfiguration.Builder("")
        MoPub.initializeSdk(this, sdkOnFiguration.build(), initSdkListener())

        moviesAdapter =
            MoviesAdapter(
                mutableListOf()
            ) { movie -> showMovieDetails(movie) }

        tvAdapter =
            TVShowsAdapter(
                mutableListOf()
            ) { show -> showTVDetails(show) }

        moviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.searchContent.searchContainer.layoutManager = moviesLayoutMgr

        genreId = intent.getIntExtra(GENRE_ID, -1)
        isMovie = intent.getBooleanExtra(IS_MOVIE, true)
        if (genreId != null && genreId != -1) {
            if (isMovie) {
                viewModel.findMovies(page, sort, genreId!!)
                setUpRecyclerView(moviesAdapter)
            } else {
                viewModel.findTVShows(page, sort, genreId!!)
                setUpRecyclerView(tvAdapter)
            }
        } else {
            finish()
        }

        viewModel.apply {
            observe(moviesList) {
                moviesAdapter.appendMovies(it)
                attachMoreOnScrollListener()
            }
            observe(showsList) {
                tvAdapter.appendMovies(it)
                attachMoreOnScrollListener()
            }
        }

        binding.search.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initSdkListener(): SdkInitializationListener? {
        return SdkInitializationListener { }
    }

    private fun attachMoreOnScrollListener() {
        binding.searchContent.searchContainer.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val totalItemCount = moviesLayoutMgr.itemCount
                    val visibleItemCount = moviesLayoutMgr.childCount
                    val firstVisibleItem = moviesLayoutMgr.findFirstVisibleItemPosition()

                    if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                        binding.searchContent.searchContainer.removeOnScrollListener(this)
                        page++
                        genreId?.let { id ->
                            if (isMovie) {
                                viewModel.findMovies(page, sort, id)
                            } else {
                                viewModel.findTVShows(page, sort, id)
                            }
                        }
                    }
                }
            })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }

    private fun showTVDetails(show: TVShow) {
        val intent = Intent(this, TVDetailsActivity::class.java)
        intent.putExtra(TVDetailsActivity.SHOW_ID, show.id)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setUpRecyclerView(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        // Pass the recycler Adapter your original adapter.
        val myMoPubAdapter = MoPubRecyclerAdapter(this, adapter)
        // Create an ad renderer and view binder that describe your native ad layout.
        val myViewBinder = ViewBinder.Builder(R.layout.mopub_native_ads_unit)
            .titleId(R.id.mopub_native_ad_title)
            .textId(R.id.mopub_native_ad_text)
            .mainImageId(R.id.mopub_native_ad_main_imageview)
            .iconImageId(R.id.mopub_native_ad_icon)
            .callToActionId(R.id.mopub_native_ad_cta)
            .privacyInformationIconImageId(R.id.mopub_native_ad_privacy)
            .sponsoredTextId(R.id.mopub_ad_sponsored_label)
            .build()

        val myRenderer = MoPubStaticNativeAdRenderer(myViewBinder)

        myMoPubAdapter.registerAdRenderer(myRenderer)

        val facebookAdRenderer = FacebookAdRenderer(
            FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.facebook_native_ads_unit)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_body)
                .mediaViewId(R.id.native_ad_media)
                .adIconViewId(R.id.native_ad_icon)
                .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                .advertiserNameId(R.id.native_ad_sponsored_label)
                .callToActionId(R.id.native_ad_call_to_action)
                .build()
        )
        myMoPubAdapter.registerAdRenderer(facebookAdRenderer)

        // Set up the RecyclerView and start loading ads
        binding.searchContent.searchContainer.adapter = myMoPubAdapter
        myMoPubAdapter.loadAds("11a17b188668469fb0412708c3d16813")
    }
}
