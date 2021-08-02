/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.NativeAdsUnitBinding
import com.aastudio.sarollahi.moviebox.databinding.RowMovieVerticalBinding
import com.bumptech.glide.Glide
import com.facebook.ads.AdOptionsView
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdsManager
import java.util.Locale

class MoviesAdapter(
    private var movies: MutableList<Movie>,
    private val adResponce: Boolean,
    private val activity: Activity?,
    private val nativeAdsManager: NativeAdsManager?,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val adItems: MutableList<NativeAd>

    init {
        adItems = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AD_TYPE && adResponce) {
            val binding =
                NativeAdsUnitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AdHolder(binding)
        } else {
            val binding =
                RowMovieVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = movies.size + adItems.size

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_DISPLAY_FREQUENCY == 0) AD_TYPE else POST_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == AD_TYPE && adResponce) {
            val ad: NativeAd?

            if (adItems.size > position / AD_DISPLAY_FREQUENCY) {
                ad = adItems[position / AD_DISPLAY_FREQUENCY]
            } else {
                ad = nativeAdsManager?.nextNativeAd()
                if (ad != null && !ad.isAdInvalidated) {
                    adItems.add(ad)
                } else {
                    Log.w(MoviesAdapter::class.java.simpleName, "Ad is invalidated!")
                }
            }

            val adHolder = holder as AdHolder
            adHolder.adChoicesContainer.removeAllViews()

            ad?.let { nonNullAd ->
                adHolder.tvAdTitle.text = nonNullAd.advertiserName
                adHolder.tvAdBody.text = nonNullAd.adBodyText
                adHolder.tvAdSocialContext.text = nonNullAd.adSocialContext
                adHolder.tvAdSponsoredLabel.setText(R.string.sponsored)
                adHolder.btnAdCallToAction.text = nonNullAd.adCallToAction
                adHolder.btnAdCallToAction.visibility =
                    if (nonNullAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
                val adOptionsView = AdOptionsView(activity, nonNullAd, adHolder.nativeAdLayout.root)
                adHolder.adChoicesContainer.addView(adOptionsView, 0)

                val clickableViews = ArrayList<View>()
                clickableViews.add(adHolder.ivAdIcon)
                clickableViews.add(adHolder.mvAdMedia)
                clickableViews.add(adHolder.btnAdCallToAction)
                nonNullAd.registerViewForInteraction(
                    adHolder.nativeAdLayout.root,
                    adHolder.mvAdMedia,
                    adHolder.ivAdIcon,
                    clickableViews
                )
            }
        } else {
            // Calculate where the next postItem index is by subtracting ads we've shown.
            val index = position - position / AD_DISPLAY_FREQUENCY - 1
            (holder as MovieViewHolder).bind(movies[index])
        }
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: RowMovieVerticalBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.moviePoster
        private var title = itemView.movieTitle
        private var release = itemView.movieRelease
        private var overview = itemView.movieOverview
        private var ratingNumber = itemView.movieRating

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                reuse()
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .into(poster)
                release.text = if (movie.releaseDate == "" || movie.releaseDate.isNullOrEmpty()) {
                    movie.originalLanguage
                } else {
                    movie.releaseDate?.let {
                        "${it.substring(0, 4)} | ${
                        movie.originalLanguage?.uppercase(
                            Locale.getDefault()
                        )
                        }"
                    }
                }
                title.text = movie.title
                overview.text = movie.overview
                ratingNumber.text = movie.rating.toString()
                itemView.setOnClickListener { onMovieClick.invoke(movie) }
            }
        }

        private fun reuse() {
            title.text = ""
            release.text = ""
            overview.text = ""
            ratingNumber.text = ""
        }
    }

    private class AdHolder(var nativeAdLayout: NativeAdsUnitBinding) :
        RecyclerView.ViewHolder(nativeAdLayout.root) {
        var mvAdMedia = nativeAdLayout.nativeAdMedia
        var ivAdIcon = nativeAdLayout.nativeAdIcon
        var tvAdTitle = nativeAdLayout.nativeAdTitle
        var tvAdBody = nativeAdLayout.nativeAdBody
        var tvAdSocialContext = nativeAdLayout.nativeAdSocialContext
        var tvAdSponsoredLabel = nativeAdLayout.nativeAdSponsoredLabel
        var btnAdCallToAction = nativeAdLayout.nativeAdCallToAction
        var adChoicesContainer = nativeAdLayout.adChoicesContainer
    }

    companion object {
        private const val AD_DISPLAY_FREQUENCY = 5
        private const val POST_TYPE = 0
        private const val AD_TYPE = 1
    }
}
