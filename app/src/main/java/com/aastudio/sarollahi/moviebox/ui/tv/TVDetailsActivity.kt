/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.common.util.FirebaseUtils
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.GenreAdapter
import com.aastudio.sarollahi.moviebox.adapter.QualityAdapter
import com.aastudio.sarollahi.moviebox.adapter.TVDetailsTabsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityTvDetailsBinding
import com.aastudio.sarollahi.moviebox.ui.authentication.AuthActivity
import com.aastudio.sarollahi.moviebox.ui.player.StreamActivity
import com.aastudio.sarollahi.moviebox.ui.player.TrailerActivity
import com.aastudio.sarollahi.moviebox.ui.search.GenreSearchActivity
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.TVViewModel
import com.aastudio.sarollahi.moviebox.util.ViewPager2ViewHeightAnimator
import com.aastudio.sarollahi.moviebox.util.createDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class TVDetailsActivity : AppCompatActivity() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var qualityAdapter: QualityAdapter
    private val viewModel by viewModel<TVViewModel>()
    private var show: TVShow? = null
    private lateinit var binding: ActivityTvDetailsBinding
    private var isInWatchlist = false

    companion object {
        const val SHOW_ID = "extra_show_id"
        private const val PLUS_ROTATION = 0f
        private const val CROSS_ROTATION = 225f
        private const val ANIMATION_DURATION_MS = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvDetailsBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.movieTitle.setSelected(true)

        val extras = intent.getStringExtra(SHOW_ID)?.toLong() ?: 0L
        if (extras != 0L) {
            viewModel.getShowDetails(extras)
        } else {
            finish()
        }

        qualityAdapter = QualityAdapter(mutableListOf()) { movie ->
            val intent = Intent(this, StreamActivity::class.java)
            intent.putExtra(StreamActivity.MOVIE_URL, movie.url)
            intent.putExtra(StreamActivity.MOVIE_TITLE, viewModel.show.value?.name)
            startActivity(intent)
        }

        binding.play.setOnClickListener {
            showMovieQualityDialog(it.rootView)
        }

        viewModel.apply {
            observe(show) { tvShow ->
                this@TVDetailsActivity.show = tvShow
                if (FirebaseUtils.firebaseAuth.currentUser != null) {
                    checkWatchList()
                }
                Glide.with(applicationContext)
                    .load("$IMAGE_ADDRESS${tvShow.backdropPath}")
                    .transform(CenterCrop())
                    .into(binding.movieBackdrop)

                Glide.with(applicationContext)
                    .load("$IMAGE_ADDRESS${tvShow.posterPath}")
                    .into(binding.moviePoster)
                binding.movieTitle.text = tvShow.name
                tvShow.rating?.let { rating -> binding.movieRating.rating = rating }
                binding.movieReleaseDate.text =
                    getString(R.string.tv_release_date, tvShow.firstAirDate, tvShow.lastAirDate)
                if (!tvShow.runTime.isNullOrEmpty()) {
                    tvShow.runTime?.get(0)?.let {
                        binding.movieRuntime.text = refactorTime(this@TVDetailsActivity, it.toInt())
                    }
                }

                binding.movieGenre.layoutManager = LinearLayoutManager(
                    this@TVDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val genreAdapter = GenreAdapter(mutableListOf()) { genre -> searchMovies(genre) }
                binding.movieGenre.adapter = genreAdapter
                tvShow.genre?.let {
                    if (it.isNotEmpty() && genreAdapter.itemCount == 0) {
                        genreAdapter.appendGenre(it)
                        genreAdapter.notifyItemRangeChanged(0, it.size - 1)
                    }
                }

                val tabArrayList =
                    arrayListOf(
                        getString(R.string.info),
                        getString(R.string.review)
                    )
                val adapter = TVDetailsTabsAdapter(
                    supportFragmentManager,
                    2,
                    tvShow,
                    lifecycle
                )
                binding.viewPager.adapter = adapter

                TabLayoutMediator(binding.detailTabs, binding.viewPager) { tab, position ->
                    tab.text = tabArrayList[position]
                }.attach()

                val fixPager2ViewHeightAnimator = ViewPager2ViewHeightAnimator()
                fixPager2ViewHeightAnimator.viewPager2 = binding.viewPager
                fixPager2ViewHeightAnimator.recalculate(binding.detailTabs.selectedTabPosition)

                if (!tvShow.trailer?.results.isNullOrEmpty()) {
                    binding.trailer.setOnClickListener {
                        tvShow.trailer?.results?.get(0)?.key?.let { key ->
                            playTrailer(key)
                        }
                    }
                }

                tvShow.externalIds?.imdbId?.let { imdbId ->
                    binding.imdb.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(getString(R.string.imdb_url, imdbId))
                        startActivity(intent)
                    }
                }

                tvShow.externalIds?.facebookId?.let { facebookId ->
                    binding.facebook.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(getString(R.string.facebook_url, facebookId))
                        startActivity(intent)
                    }
                }

                tvShow.externalIds?.instagramId?.let { instagramId ->
                    binding.instagram.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(getString(R.string.instagram_url, instagramId))
                        startActivity(intent)
                    }
                }

                tvShow.externalIds?.twitterId?.let { twitterId ->
                    binding.twitter.setOnClickListener {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse(getString(R.string.twitter_url, twitterId))
                        startActivity(intent)
                    }
                }
            }
            observe(torrentTv) {
                if (it.isNotEmpty()) {
                    binding.playContainer.visibility = View.VISIBLE
                    qualityAdapter.appendQuality(it)
                    qualityAdapter.notifyItemRangeChanged(0, it.size - 1)
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
            if (isInWatchlist) {
                val ref = FirebaseDatabase.getInstance().reference
                val applesQuery: Query =
                    ref.child("watchList").child("${FirebaseUtils.firebaseAuth.currentUser?.uid}")
                        .orderByChild("id").equalTo(show?.id)

                applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (show in dataSnapshot.children) {
                            show.ref.removeValue()
                            updateWatchListBtn(isInWatchlist = false)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } else {
                if (FirebaseUtils.firebaseAuth.currentUser != null) {
                    val ref = FirebaseDatabase.getInstance()
                        .getReference("watchList")
                        .child("${FirebaseUtils.firebaseAuth.currentUser?.uid}").push()
                    ref.child("id").setValue(show?.id)
                    ref.child("title").setValue(show?.name)
                    ref.child("releaseDate").setValue(show?.firstAirDate)
                    ref.child("rating").setValue(show?.rating.toString())
                    ref.child("overview").setValue(show?.overview)
                    ref.child("posterPath").setValue(show?.posterPath)
                    ref.child("originalLanguage").setValue(show?.originalLanguage)
                    ref.child("type").setValue("Show")
                    updateWatchListBtn(isInWatchlist = true)
                } else {
                    createDialog(
                        this,
                        getString(R.string.access_denied),
                        getString(R.string.access_denied_message),
                        {
                            startActivity(Intent(this, AuthActivity::class.java))
                            it.dismiss()
                        },
                        {
                            it.dismiss()
                        }
                    )
                }
            }
        }

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val bitmap = getBitmapFromView(binding.moviePoster)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${viewModel.show.value?.name}\n${getString(R.string.app_name)}\n${
                getString(
                    R.string.play_url,
                    packageName
                )
                }"
            )
            intent.putExtra(Intent.EXTRA_STREAM, getImageURI(this@TVDetailsActivity, bitmap))
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
            dialogTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
            dialogTitle.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.bodyTextBackground
                )
            )
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
            drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                tint,
                BlendModeCompat.SRC_IN
            )
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
        val intent = Intent(this, GenreSearchActivity::class.java)
        intent.putExtra(GenreSearchActivity.GENRE_NAME, "${genre.name} TV Shows")
        intent.putExtra(GenreSearchActivity.GENRE_ID, genre.id)
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
                viewModel.show.value?.name,
                null
            )
        return Uri.parse(path)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun checkWatchList() {
        val ref = FirebaseDatabase.getInstance().reference
        val applesQuery: Query =
            ref.child("watchList").child("${FirebaseUtils.firebaseAuth.currentUser?.uid}")
                .orderByChild("id").equalTo(show?.id)

        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (show in dataSnapshot.children) {
                    updateWatchListBtn(show.exists())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("iamhere", "onCancelled", databaseError.toException())
            }
        })
    }
}
