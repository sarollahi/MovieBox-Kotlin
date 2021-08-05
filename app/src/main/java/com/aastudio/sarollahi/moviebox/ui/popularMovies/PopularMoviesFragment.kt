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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.POPULAR_ADS_PLACEMENT_ID
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.Repository
import com.aastudio.sarollahi.api.response.GetMoviesResponse
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentPopularMoviesBinding
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity.Companion.MOVIE_ID
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager
import retrofit2.Call

class PopularMoviesFragment : Fragment(), NativeAdsManager.Listener {
    private var _binding: FragmentPopularMoviesBinding? = null
    private var nativeAdsManager: NativeAdsManager? = null
    private lateinit var popularMovies: RecyclerView
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

        nativeAdsManager = NativeAdsManager(activity, POPULAR_ADS_PLACEMENT_ID, 5)
        nativeAdsManager?.loadAds()
        nativeAdsManager?.setListener(this)

        popularMovies = binding.popularMovies

        popularMoviesLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        popularMovies.layoutManager = popularMoviesLayoutMgr

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPopularMovies() {
        Repository.getPopularMovies(
            popularMoviesPage,
            "us",
            ::onPopularMoviesFetched,
            ::onError
        )
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun onError(call: Call<GetMoviesResponse>, error: String) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.error_fetch_movies),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        startActivity(intent)
    }

    override fun onAdsLoaded() {
        popularMoviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                true,
                this.requireActivity(),
                nativeAdsManager
            ) { movie -> showMovieDetails(movie) }
        popularMovies.adapter = popularMoviesAdapter

        getPopularMovies()
    }

    override fun onAdError(error: AdError) {
        popularMoviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                false,
                null,
                null
            ) { movie -> showMovieDetails(movie) }
        popularMovies.adapter = popularMoviesAdapter

        getPopularMovies()
    }
}
