/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.rootMovie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.GenreAdapter
import com.aastudio.sarollahi.moviebox.adapter.RootPlayingMoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.RootPopularMoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.RootTopRatedMoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.RootUpcommingMoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentRootMovieBinding
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.search.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootMovieFragment : Fragment() {

    private val viewModel by viewModel<RootMovieViewModel>()
    private lateinit var binding: FragmentRootMovieBinding
    private var upcomingMoviesAdapter: RootUpcommingMoviesAdapter? = null
    private var playingMoviesAdapter: RootPlayingMoviesAdapter? = null
    private var popularMoviesAdapter: RootPopularMoviesAdapter? = null
    private var topRatedMoviesAdapter: RootTopRatedMoviesAdapter? = null
    private var genreAdapter: GenreAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRootMovieBinding.inflate(inflater, container, false)
        setUpUI()

        viewModel.getMovies()
        viewModel.getGenres()

        viewModel.apply {
            observe(upcomingList) {
                if (it.isNotEmpty()) {
                    setUpcomingMovies(it)
                }
            }
            observe(nowPlayingList) {
                if (it.isNotEmpty()) {
                    setPlayingMovies(it)
                }
            }
            observe(popularList) {
                if (it.isNotEmpty()) {
                    setPopularMovies(it)
                }
            }
            observe(topRatedList) {
                if (it.isNotEmpty()) {
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
            Navigation.findNavController(it).navigate(R.id.action_nav_root_to_nav_toprated)
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
            RootUpcommingMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.upcomingMovies.adapter = upcomingMoviesAdapter

        // now playing
        binding.nowPlayingMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        playingMoviesAdapter =
            RootPlayingMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.nowPlayingMovies.adapter = playingMoviesAdapter

        // popular
        binding.popularMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesAdapter =
            RootPopularMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.popularMovies.adapter = popularMoviesAdapter

        // topRated
        binding.topRatedMovies.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMoviesAdapter =
            RootTopRatedMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
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
        upcomingMoviesAdapter?.notifyDataSetChanged()
    }

    private fun setPlayingMovies(movies: List<Movie>) {
        playingMoviesAdapter?.appendMovies(movies)
        playingMoviesAdapter?.notifyDataSetChanged()
    }

    private fun setPopularMovies(movies: List<Movie>) {
        popularMoviesAdapter?.appendMovies(movies)
        popularMoviesAdapter?.notifyDataSetChanged()
    }

    private fun setTopRatedMovies(movies: List<Movie>) {
        topRatedMoviesAdapter?.appendMovies(movies)
        topRatedMoviesAdapter?.notifyDataSetChanged()
    }

    private fun setMovieGenres(genres: List<Genre>) {
        if (genreAdapter?.itemCount == 0) {
            genreAdapter?.appendGenre(genres)
            genreAdapter?.notifyDataSetChanged()
        }
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }

    private fun searchMovies(genre: Genre) {
        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.GENRE_NAME, "${genre.name} Movies")
        intent.putExtra(SearchActivity.GENRE_ID, genre.id)
        startActivity(intent)
    }
}
