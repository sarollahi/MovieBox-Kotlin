/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movieDetails

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.DetailsTabsAdapter
import com.aastudio.sarollahi.moviebox.adapter.GenreAdapter
import com.aastudio.sarollahi.moviebox.adapter.QualityAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityMovieDetailsBinding
import com.aastudio.sarollahi.moviebox.ui.player.StreamActivity
import com.aastudio.sarollahi.moviebox.ui.player.StreamActivity.Companion.MOVIE_TITLE
import com.aastudio.sarollahi.moviebox.ui.player.StreamActivity.Companion.MOVIE_URL
import com.aastudio.sarollahi.moviebox.ui.search.SearchActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("DEPRECATION")
class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var qualityAdapter: QualityAdapter
    private val viewModel by viewModel<MovieViewModel>()
    private lateinit var binding: ActivityMovieDetailsBinding
    private var isInWatchlist = false

    companion object {
        const val MOVIE_ID = "extra_movie_id"
        private const val PLUS_ROTATION = 0f
        private const val CROSS_ROTATION = 225f
        private const val ANIMATION_DURATION_MS = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.movieTitle.setSelected(true)

        val extras = intent.getLongExtra(MOVIE_ID, 0L)
        if (extras != 0L) {
            viewModel.getMovieDetails(extras)
        } else {
            finish()
        }

        qualityAdapter = QualityAdapter(mutableListOf()) { movie ->
            val intent = Intent(this, StreamActivity::class.java)
            intent.putExtra(MOVIE_URL, movie.url)
            intent.putExtra(MOVIE_TITLE, viewModel.movie.value?.title)
            startActivity(intent)
        }

        binding.play.setOnClickListener {
            showMovieQualityDialog(it.rootView)
        }

        viewModel.apply {
            observe(movie) { movie ->
                Glide.with(applicationContext)
                    .load("$IMAGE_ADDRESS${movie.backdropPath}")
                    .transform(CenterCrop())
                    .into(binding.movieBackdrop)

                Glide.with(applicationContext)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .into(binding.moviePoster)
                binding.movieTitle.text = movie.title
                movie.rating?.let { rating -> binding.movieRating.rating = rating }
                binding.movieReleaseDate.text = movie.releaseDate
                movie.runTime?.let { runTime -> binding.movieRuntime.text = refactorTime(runTime) }

                binding.movieGenre.layoutManager = LinearLayoutManager(
                    this@MovieDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val genreAdapter = GenreAdapter(mutableListOf()) { genre -> searchMovies(genre) }
                binding.movieGenre.adapter = genreAdapter
                movie.genre?.let {
                    if (it.isNotEmpty() && genreAdapter.itemCount == 0) {
                        genreAdapter.appendGenre(it)
                        genreAdapter.notifyDataSetChanged()
                    }
                }

                binding.detailTabs.newTab().setText(R.string.info)
                    .let { binding.detailTabs.addTab(it) }
                binding.detailTabs.newTab().setText(R.string.review)
                    .let { binding.detailTabs.addTab(it) }
                val adapter = binding.detailTabs.tabCount.let {
                    DetailsTabsAdapter(
                        this@MovieDetailsActivity, supportFragmentManager,
                        it,
                        movie
                    )
                }
                binding.viewPager.adapter = adapter
                binding.viewPager.addOnPageChangeListener(
                    TabLayout.TabLayoutOnPageChangeListener(
                        binding.detailTabs
                    )
                )
                binding.detailTabs.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab) {
                            binding.viewPager.currentItem = tab.position
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab) {}
                        override fun onTabReselected(tab: TabLayout.Tab) {}
                    })
            }
            observe(torrentTv) {
                if (it.isNotEmpty()) {
                    binding.play.visibility = View.VISIBLE
                    qualityAdapter.appendQuality(it)
                    qualityAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.watchList.setOnClickListener {
            if (isInWatchlist) updateWatchListBtn(
                isInWatchlist = false,
                animate = true
            ) else updateWatchListBtn(isInWatchlist = true, animate = true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showMovieQualityDialog(view: View) {
        val viewGroup: ViewGroup? = view.findViewById(android.R.id.content)
        val dialogView = layoutInflater.inflate(R.layout.movie_dialog, viewGroup, false)
        AlertDialog.Builder(view.context).apply {
            val titleView = layoutInflater.inflate(R.layout.row_text, viewGroup, false)
            val dialogTitle = titleView.findViewById<TextView>(R.id.generalText)
            dialogTitle.text = resources.getString(R.string.movieQuality)
            dialogTitle.setTextColor(resources.getColor(R.color.white))
            dialogTitle.setBackgroundColor(resources.getColor(R.color.bodyTextBackground))
            setCustomTitle(titleView)
            setView(dialogView)
            alertDialog = create()
        }
        val movieDialogRV = dialogView.findViewById<RecyclerView>(R.id.movieDialogRV)
        movieDialogRV.adapter = qualityAdapter
        alertDialog.show()
    }

    private fun updateWatchListBtn(isInWatchlist: Boolean, animate: Boolean) {
        this.isInWatchlist = isInWatchlist
        val (rotation, tint) = if (isInWatchlist) {
            CROSS_ROTATION to Color.BLACK
        } else {
            PLUS_ROTATION to Color.WHITE
        }
        binding.watchList.apply {
            if (animate) rotatePlusIcon(rotation) else this.rotation = rotation
            isSelected = isInWatchlist
            drawable.setColorFilter(tint, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun rotatePlusIcon(degrees: Float) {
        binding.watchList.animate().apply {
            cancel()
            rotation(degrees)
            duration = ANIMATION_DURATION_MS
            interpolator = OvershootInterpolator()
            start()
        }
    }

    private fun searchMovies(genre: Genre) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(SearchActivity.GENRE_NAME, "${genre.name} Movies")
        intent.putExtra(SearchActivity.GENRE_ID, genre.id)
        startActivity(intent)
    }
}
