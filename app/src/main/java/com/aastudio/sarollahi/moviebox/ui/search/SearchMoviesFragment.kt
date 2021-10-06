/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.TOP_RATED_ADS_PLACEMENT_ID
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentVerticalRecyclerViewBinding
import com.aastudio.sarollahi.moviebox.ui.movie.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.search.viewModel.SearchViewModel
import com.facebook.ads.AudienceNetworkAds
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.nativeads.FacebookAdRenderer
import com.mopub.nativeads.MoPubRecyclerAdapter
import com.mopub.nativeads.MoPubStaticNativeAdRenderer
import com.mopub.nativeads.ViewBinder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMoviesFragment : Fragment() {
    lateinit var binding: FragmentVerticalRecyclerViewBinding
    private val viewModel by viewModel<SearchViewModel>()
    private var movies: List<Movie>? = null
    private lateinit var adapter: MoviesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            movies = it.getParcelableArrayList(MOVIE_LIST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerticalRecyclerViewBinding.inflate(inflater, container, false)

        AudienceNetworkAds.initialize(requireContext())
        val sdkOnFiguration = SdkConfiguration.Builder("")
        MoPub.initializeSdk(requireContext(), sdkOnFiguration.build(), initSdkListener())

        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.layoutManager = layoutManager

        adapter =
            MoviesAdapter(
                mutableListOf()
            ) { movie -> showMovieDetails(movie) }

        movies?.apply {
            adapter.appendMovies(this)
            attachMoreOnScrollListener()
        }

        viewModel.apply {
            observe(moviesList) {
                Log.d("iamhere observe", it.toString())
                adapter.appendMovies(it)
                attachMoreOnScrollListener()
            }
        }

        setUpRecyclerView(adapter)

        return binding.root
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener { }
    }

    private fun attachMoreOnScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.recyclerView.removeOnScrollListener(this)
                    page++
                    viewModel.searchMovie(
                        page,
                        activity?.intent?.getStringExtra(SearchActivity.TITLE)!!,
                        activity?.intent?.getStringExtra(SearchActivity.YEAR)!!
                    )
                }
            }
        })
    }

    private fun setUpRecyclerView(adapter: MoviesAdapter) {
        // Pass the recycler Adapter your original adapter.
        val myMoPubAdapter = MoPubRecyclerAdapter(requireActivity(), adapter)
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
        binding.recyclerView.adapter = myMoPubAdapter
        myMoPubAdapter.loadAds(TOP_RATED_ADS_PLACEMENT_ID)
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }

    companion object {
        private const val MOVIE_LIST = "movieList"

        fun newInstance(moviesList: ArrayList<Movie>?): SearchMoviesFragment {
            val fragment = SearchMoviesFragment()
            val args = Bundle()
            args.putParcelableArrayList(MOVIE_LIST, moviesList)
            fragment.arguments = args
            return fragment
        }
    }
}