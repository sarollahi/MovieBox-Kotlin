package com.aastudio.sarollahi.moviebox.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.api.model.Movie
import com.bumptech.glide.Glide
import com.facebook.ads.*
import java.util.*

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
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.native_ads_unit, parent, false) as NativeAdLayout
            AdHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_movie_vertical, parent, false)
            MovieViewHolder(view)
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
                val adOptionsView = AdOptionsView(activity, nonNullAd, adHolder.nativeAdLayout)
                adHolder.adChoicesContainer.addView(adOptionsView, 0)

                val clickableViews = ArrayList<View>()
                clickableViews.add(adHolder.ivAdIcon)
                clickableViews.add(adHolder.mvAdMedia)
                clickableViews.add(adHolder.btnAdCallToAction)
                nonNullAd.registerViewForInteraction(
                    adHolder.nativeAdLayout, adHolder.mvAdMedia, adHolder.ivAdIcon, clickableViews
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

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        private var title: TextView = itemView.findViewById(R.id.movieTitle)
        private var release: TextView = itemView.findViewById(R.id.movieRelease)
        private var overview: TextView = itemView.findViewById(R.id.movieOverview)
        private var ratingNumber: TextView = itemView.findViewById(R.id.movieRating)

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .into(poster)
                release.text = if (movie.releaseDate == "" || movie.releaseDate.isNullOrEmpty()) {
                    movie.originalLanguage
                } else {
                    movie.releaseDate?.let {
                        "${it.substring(0, 4)} | ${
                            movie.originalLanguage?.toUpperCase(
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
    }

    private class AdHolder(var nativeAdLayout: NativeAdLayout) :
        RecyclerView.ViewHolder(nativeAdLayout) {
        var mvAdMedia: MediaView =
            nativeAdLayout.findViewById(R.id.native_ad_media)
        var ivAdIcon: MediaView = nativeAdLayout.findViewById(R.id.native_ad_icon)
        var tvAdTitle: TextView = nativeAdLayout.findViewById(R.id.native_ad_title)
        var tvAdBody: TextView = nativeAdLayout.findViewById(R.id.native_ad_body)
        var tvAdSocialContext: TextView =
            nativeAdLayout.findViewById(R.id.native_ad_social_context)
        var tvAdSponsoredLabel: TextView =
            nativeAdLayout.findViewById(R.id.native_ad_sponsored_label)
        var btnAdCallToAction: Button =
            nativeAdLayout.findViewById(R.id.native_ad_call_to_action)
        var adChoicesContainer: LinearLayout =
            nativeAdLayout.findViewById(R.id.ad_choices_container)
    }

    companion object {
        private const val AD_DISPLAY_FREQUENCY = 5
        private const val POST_TYPE = 0
        private const val AD_TYPE = 1
    }
}