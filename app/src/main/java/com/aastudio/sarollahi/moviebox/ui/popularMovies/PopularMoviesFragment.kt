/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.popularMovies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.POPULAR_ADS_PLACEMENT_ID
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentPopularMoviesBinding
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity.Companion.MOVIE_ID
import com.facebook.ads.AudienceNetworkAds
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.nativeads.FacebookAdRenderer
import com.mopub.nativeads.MoPubRecyclerAdapter
import com.mopub.nativeads.MoPubStaticNativeAdRenderer
import com.mopub.nativeads.ViewBinder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularMoviesFragment : Fragment() {
    private val viewModel by viewModel<PopularMoviesViewModel>()
    private var _binding: FragmentPopularMoviesBinding? = null
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private var popularMoviesPage = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularMoviesBinding.inflate(inflater, container, false)

        AudienceNetworkAds.initialize(requireContext())
        val sdkOnFiguration = SdkConfiguration.Builder("")
        MoPub.initializeSdk(requireContext(), sdkOnFiguration.build(), initSdkListener())

        popularMoviesLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.popularMovies.layoutManager = popularMoviesLayoutMgr

        popularMoviesAdapter =
            MoviesAdapter(
                mutableListOf()
            ) { movie -> showMovieDetails(movie) }

        viewModel.apply {
            getMovies(popularMoviesPage)

            observe(popularList) {
                popularMoviesAdapter.appendMovies(it)
                attachPopularMoviesOnScrollListener()
            }
        }
        setUpRecyclerView(popularMoviesAdapter)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener { }
    }

    private fun attachPopularMoviesOnScrollListener() {
        binding.popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    viewModel.getMovies(popularMoviesPage)
                }
            }
        })
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        startActivity(intent)
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
        binding.popularMovies.adapter = myMoPubAdapter
        myMoPubAdapter.loadAds(POPULAR_ADS_PLACEMENT_ID)
    }
}
