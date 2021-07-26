package com.aastudio.sarollahi.moviebox.ui.topRatedMovies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.MoviesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentTopRatedMoviesBinding
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.repository.MoviesRepository
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity.Companion.MOVIE_ID
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager

class TopRatedMoviesFragment : Fragment(), NativeAdsManager.Listener {

    private lateinit var homeViewModel: TopRatedMoviesViewModel
    private var _binding: FragmentTopRatedMoviesBinding? = null
    private var nativeAdsManager: NativeAdsManager? = null
    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager

    private var topRatedMoviesPage = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(TopRatedMoviesViewModel::class.java)

        _binding = FragmentTopRatedMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val placementId = ""
        nativeAdsManager = NativeAdsManager(activity, placementId, 5)
        nativeAdsManager?.loadAds()
        nativeAdsManager?.setListener(this)

        topRatedMovies = root.findViewById(R.id.top_rated_movies)

        topRatedMoviesLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(context, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_ID, movie.id)
        startActivity(intent)
    }

    override fun onAdsLoaded() {
        topRatedMoviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                true,
                this.requireActivity(),
                nativeAdsManager
            ) { movie -> showMovieDetails(movie) }
        topRatedMovies.adapter = topRatedMoviesAdapter

        getTopRatedMovies()
    }

    override fun onAdError(error: AdError) {
        topRatedMoviesAdapter =
            MoviesAdapter(
                mutableListOf(),
                false,
                null,
                null
            ) { movie -> showMovieDetails(movie) }
        topRatedMovies.adapter = topRatedMoviesAdapter

        getTopRatedMovies()
    }
}