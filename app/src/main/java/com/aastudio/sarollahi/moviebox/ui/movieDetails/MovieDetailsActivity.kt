/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movieDetails

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
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
import com.aastudio.sarollahi.moviebox.ui.player.TrailerActivity
import com.aastudio.sarollahi.moviebox.ui.search.SearchActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

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

                if (!movie.trailer?.results.isNullOrEmpty()){
                    binding.trailer.setOnClickListener {
                        movie.trailer?.results?.get(0)?.key?.let { key ->
                            playTrailer(key)
                        }
                    }
                }

                movie.externalIds?.imdbId?.let { imdbId ->
                    binding.imdb.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://www.imdb.com/title/$imdbId/")
                        startActivity(intent)
                    }
                }

                movie.externalIds?.facebookId?.let { facebookId ->
                    binding.facebook.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://www.facebook.com/$facebookId/")
                        startActivity(intent)
                    }
                }

                movie.externalIds?.instagramId?.let { instagramId ->
                    binding.instagram.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://www.instagram.com/$instagramId/")
                        startActivity(intent)
                    }
                }

                movie.externalIds?.twitterId?.let { twitterId ->
                    binding.twitter.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://twitter.com/$twitterId/")
                        startActivity(intent)
                    }
                }
            }
            observe(torrentTv) {
                if (it.isNotEmpty()) {
                    binding.playContainer.visibility = View.VISIBLE
                    qualityAdapter.appendQuality(it)
                    qualityAdapter.notifyDataSetChanged()
                }
            }
            observe(loading) {
                if (!it) {
                    binding.loading.visibility = View.GONE
                    binding.detailContainer.visibility = View.VISIBLE
                }
            }
        }

        binding.watchList.setOnClickListener {
            if (isInWatchlist) updateWatchListBtn(
                isInWatchlist = false
            ) else updateWatchListBtn(isInWatchlist = true)
        }

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val bitmap = getBitmapFromView(binding.moviePoster)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${viewModel.movie.value?.title}\n${getString(R.string.app_name)}\nhttps://play.google.com/store/apps/details?id=com.aastudio.sarollahi.moviebox"
            )
            intent.putExtra(Intent.EXTRA_STREAM, getImageURI(this@MovieDetailsActivity, bitmap))
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, getText(R.string.share)))
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

    private fun updateWatchListBtn(isInWatchlist: Boolean, animate: Boolean = true) {
        this.isInWatchlist = isInWatchlist
        val (rotation, tint) = if (isInWatchlist) {
            CROSS_ROTATION to Color.BLACK
        } else {
            PLUS_ROTATION to ContextCompat.getColor(this, R.color.duskYellow)
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

    private fun playTrailer(key: String) {
        val intent = Intent(this, TrailerActivity::class.java)
        intent.putExtra(TrailerActivity.KEY, key)
        startActivity(intent)
    }

    private fun getBitmapFromView(view: ImageView): Bitmap {
        val bitmap = createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getImageURI(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                viewModel.movie.value?.title,
                null
            )
        return Uri.parse(path)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
