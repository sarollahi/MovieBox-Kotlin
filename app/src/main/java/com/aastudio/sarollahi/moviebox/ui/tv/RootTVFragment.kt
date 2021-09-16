/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.network.NetworkUtils
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.GenreAdapter
import com.aastudio.sarollahi.moviebox.adapter.TVHorizontalLargeAdapter
import com.aastudio.sarollahi.moviebox.adapter.TVHorizontalSmallAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentRootBinding
import com.aastudio.sarollahi.moviebox.ui.search.SearchActivity
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.RootTVViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootTVFragment : Fragment() {

    private val viewModel by viewModel<RootTVViewModel>()
    private lateinit var binding: FragmentRootBinding
    private var upcomingShowsAdapter: TVHorizontalLargeAdapter? = null
    private var playingShowsAdapter: TVHorizontalSmallAdapter? = null
    private var popularShowsAdapter: TVHorizontalSmallAdapter? = null
    private var topRatedShowsAdapter: TVHorizontalSmallAdapter? = null
    private var genreAdapter: GenreAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRootBinding.inflate(inflater, container, false)
        setUpUI()

        if (NetworkUtils.checkIsOnline(requireContext())) {
            viewModel.apply {
                getShows()
                getGenres()
            }
        } else {
            binding.networkError.errorContainer.visibility = View.VISIBLE
            binding.networkError.retryLayout.retryBtn.setOnClickListener {
                binding.networkError.loadingPb.visibility = View.VISIBLE
                if (NetworkUtils.checkIsOnline(requireContext())) {
                    viewModel.apply {
                        binding.networkError.errorContainer.visibility = View.GONE
                        getShows()
                        getGenres()
                    }
                } else {
                    binding.networkError.loadingPb.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        R.string.error_message_device_offline,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.apply {
            observe(upcomingList) {
                if (it.isNotEmpty()) {
                    binding.upcommingLoading.visibility = View.GONE
                    setUpcomingShows(it)
                }
            }
            observe(nowPlayingList) {
                if (it.isNotEmpty()) {
                    binding.nowPlayingLoading.visibility = View.GONE
                    setPlayingShows(it)
                }
            }
            observe(popularList) {
                if (it.isNotEmpty()) {
                    binding.popularLoading.visibility = View.GONE
                    setPopularShows(it)
                }
            }
            observe(topRatedList) {
                if (it.isNotEmpty()) {
                    binding.topRatedLoading.visibility = View.GONE
                    setTopRatedShows(it)
                }
            }
            observe(movieGenres) {
                if (it.isNotEmpty()) {
                    setShowGenres(it)
                }
            }
        }

        binding.upcomingMore.setOnClickListener {
            Navigation.findNavController(requireActivity(), it.id)
                .navigate(R.id.action_nav_root_to_nav_upcoming)
        }
        binding.nowPlayingMore.setOnClickListener {
            Navigation.findNavController(requireActivity(), it.id)
                .navigate(R.id.action_nav_root_to_nav_nowPlaying)
        }
        binding.popularMore.setOnClickListener {
            Navigation.findNavController(requireActivity(), it.id)
                .navigate(R.id.action_nav_root_to_nav_popular)
        }
        binding.topRatedMore.setOnClickListener {
            Navigation.findNavController(requireActivity(), it.id)
                .navigate(R.id.action_nav_root_to_nav_topRated)
        }

        return binding.root
    }

    private fun setUpUI() {
        // titles
        binding.upcomingTitle.text = requireContext().getString(R.string.on_the_air)
        binding.nowPlayingTitle.text = requireContext().getString(R.string.airing_today)

        // upcomming
        binding.upcomingMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingShowsAdapter =
            TVHorizontalLargeAdapter(mutableListOf()) { show -> showShowDetails(show) }
        binding.upcomingMovies.adapter = upcomingShowsAdapter

        // now playing
        binding.nowPlayingMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        playingShowsAdapter =
            TVHorizontalSmallAdapter(mutableListOf()) { show -> showShowDetails(show) }
        binding.nowPlayingMovies.adapter = playingShowsAdapter

        // popular
        binding.popularMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularShowsAdapter =
            TVHorizontalSmallAdapter(mutableListOf()) { show -> showShowDetails(show) }
        binding.popularMovies.adapter = popularShowsAdapter

        // topRated
        binding.topRatedMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedShowsAdapter =
            TVHorizontalSmallAdapter(mutableListOf()) { show -> showShowDetails(show) }
        binding.topRatedMovies.adapter = topRatedShowsAdapter

        // movie genres
        binding.genres.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        genreAdapter =
            GenreAdapter(mutableListOf()) { genre -> searchShows(genre) }
        binding.genres.adapter = genreAdapter
    }

    private fun setUpcomingShows(shows: List<TVShow>) {
        upcomingShowsAdapter?.appendShows(shows)
        upcomingShowsAdapter?.notifyItemRangeChanged(0, shows.size - 1)
    }

    private fun setPlayingShows(shows: List<TVShow>) {
        playingShowsAdapter?.appendShows(shows)
        playingShowsAdapter?.notifyItemRangeChanged(0, shows.size - 1)
    }

    private fun setPopularShows(shows: List<TVShow>) {
        popularShowsAdapter?.appendShows(shows)
        popularShowsAdapter?.notifyItemRangeChanged(0, shows.size - 1)
    }

    private fun setTopRatedShows(shows: List<TVShow>) {
        topRatedShowsAdapter?.appendShows(shows)
        topRatedShowsAdapter?.notifyItemRangeChanged(0, shows.size - 1)
    }

    private fun setShowGenres(genres: List<Genre>) {
        if (genreAdapter?.itemCount == 0) {
            genreAdapter?.appendGenre(genres)
            genreAdapter?.notifyItemRangeChanged(0, genres.size - 1)
        }
    }

    private fun showShowDetails(show: TVShow) {
        val intent = Intent(context, TVDetailsActivity::class.java)
        intent.putExtra(TVDetailsActivity.SHOW_ID, show.id)
        startActivity(intent)
    }

    private fun searchShows(genre: Genre) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.GENRE_NAME, "${genre.name} Shows")
        intent.putExtra(SearchActivity.GENRE_ID, genre.id)
        intent.putExtra(SearchActivity.IS_MOVIE, false)
        startActivity(intent)
    }
}
