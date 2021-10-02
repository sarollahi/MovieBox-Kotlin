/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movie

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
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.common.network.NetworkUtils
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.GenreAdapter
import com.aastudio.sarollahi.moviebox.adapter.MoviesHorizontalLargeAdapter
import com.aastudio.sarollahi.moviebox.adapter.MoviesHorizontalSmallAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentRootBinding
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.RootMovieViewModel
import com.aastudio.sarollahi.moviebox.ui.search.GenreSearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootMovieFragment : Fragment() {

    private val viewModel by viewModel<RootMovieViewModel>()
    private lateinit var binding: FragmentRootBinding
    private var upcomingMoviesAdapter: MoviesHorizontalLargeAdapter? = null
    private var playingMoviesAdapter: MoviesHorizontalSmallAdapter? = null
    private var popularMoviesAdapter: MoviesHorizontalSmallAdapter? = null
    private var topRatedMoviesAdapter: MoviesHorizontalSmallAdapter? = null
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
                getMovies()
                getGenres()
            }
        } else {
            binding.networkError.errorContainer.visibility = View.VISIBLE
            binding.networkError.retryLayout.retryBtn.setOnClickListener {
                binding.networkError.loadingPb.visibility = View.VISIBLE
                if (NetworkUtils.checkIsOnline(requireContext())) {
                    viewModel.apply {
                        binding.networkError.errorContainer.visibility = View.GONE
                        getMovies()
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
                    setUpcomingMovies(it)
                }
            }
            observe(nowPlayingList) {
                if (it.isNotEmpty()) {
                    binding.nowPlayingLoading.visibility = View.GONE
                    setPlayingMovies(it)
                }
            }
            observe(popularList) {
                if (it.isNotEmpty()) {
                    binding.popularLoading.visibility = View.GONE
                    setPopularMovies(it)
                }
            }
            observe(topRatedList) {
                if (it.isNotEmpty()) {
                    binding.topRatedLoading.visibility = View.GONE
                    setTopRatedMovies(it)
                }
            }
            observe(movieGenres) {
                if (it.isNotEmpty()) {
                    setMovieGenres(it)
                }
            }
        }

        binding.upcomingMore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_root_to_nav_upcoming)
        }
        binding.nowPlayingMore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_root_to_nav_nowPlaying)
        }
        binding.popularMore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_root_to_nav_popular)
        }
        binding.topRatedMore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_nav_root_to_nav_topRated)
        }

        return binding.root
    }

    private fun setUpUI() {
        // upcomming
        binding.upcomingMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingMoviesAdapter =
            MoviesHorizontalLargeAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.upcomingMovies.adapter = upcomingMoviesAdapter

        // now playing
        binding.nowPlayingMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        playingMoviesAdapter =
            MoviesHorizontalSmallAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.nowPlayingMovies.adapter = playingMoviesAdapter

        // popular
        binding.popularMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesAdapter =
            MoviesHorizontalSmallAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.popularMovies.adapter = popularMoviesAdapter

        // topRated
        binding.topRatedMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMoviesAdapter =
            MoviesHorizontalSmallAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.topRatedMovies.adapter = topRatedMoviesAdapter

        // movie genres
        binding.genres.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        genreAdapter =
            GenreAdapter(mutableListOf()) { genre -> searchMovies(genre) }
        binding.genres.adapter = genreAdapter
    }

    private fun setUpcomingMovies(movies: List<Movie>) {
        upcomingMoviesAdapter?.appendMovies(movies)
        upcomingMoviesAdapter?.notifyItemRangeChanged(0, movies.size - 1)
    }

    private fun setPlayingMovies(movies: List<Movie>) {
        playingMoviesAdapter?.appendMovies(movies)
        playingMoviesAdapter?.notifyItemRangeChanged(0, movies.size - 1)
    }

    private fun setPopularMovies(movies: List<Movie>) {
        popularMoviesAdapter?.appendMovies(movies)
        popularMoviesAdapter?.notifyItemRangeChanged(0, movies.size - 1)
    }

    private fun setTopRatedMovies(movies: List<Movie>) {
        topRatedMoviesAdapter?.appendMovies(movies)
        topRatedMoviesAdapter?.notifyItemRangeChanged(0, movies.size - 1)
    }

    private fun setMovieGenres(genres: List<Genre>) {
        if (genreAdapter?.itemCount == 0) {
            genreAdapter?.appendGenre(genres)
            genreAdapter?.notifyItemRangeChanged(0, genres.size - 1)
        }
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }

    private fun searchMovies(genre: Genre) {
        val intent = Intent(context, GenreSearchActivity::class.java)
        intent.putExtra(GenreSearchActivity.GENRE_NAME, "${genre.name} Movies")
        intent.putExtra(GenreSearchActivity.GENRE_ID, genre.id)
        startActivity(intent)
    }
}
