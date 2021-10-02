/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.databinding.RowMovieVerticalBinding
import com.bumptech.glide.Glide
import java.util.Locale

class TVShowsAdapter(
    private var tvShows: MutableList<TVShow>,
    private val onItemClick: (show: TVShow) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RowMovieVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = tvShows.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(tvShows[position])
    }

    fun appendMovies(tvShows: List<TVShow>) {
        this.tvShows.addAll(tvShows)
        notifyItemRangeInserted(
            this.tvShows.size,
            tvShows.size - 1
        )
    }

    inner class MovieViewHolder(itemView: RowMovieVerticalBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.moviePoster
        private var title = itemView.movieTitle
        private var release = itemView.movieRelease
        private var overview = itemView.movieOverview
        private var ratingNumber = itemView.movieRating

        fun bind(show: TVShow) {
            if (!show.posterPath.isNullOrEmpty()) {
                reuse()
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${show.posterPath}")
                    .into(poster)
                release.text = if (show.firstAirDate == "" || show.firstAirDate.isNullOrEmpty()) {
                    show.originalLanguage
                } else {
                    show.firstAirDate?.let {
                        "${it.substring(0, 4)} | ${
                        show.originalLanguage?.uppercase(
                            Locale.getDefault()
                        )
                        }"
                    }
                }
                title.text = show.name
                overview.text = show.overview
                ratingNumber.text = show.rating.toString()
                itemView.setOnClickListener { onItemClick.invoke(show) }
            }
        }

        private fun reuse() {
            title.text = ""
            release.text = ""
            overview.text = ""
            ratingNumber.text = ""
            poster.setImageDrawable(null)
        }
    }
}
