/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.databinding.RowMovieVerticalBinding
import com.bumptech.glide.Glide
import java.util.Locale

class MoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onItemClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RowMovieVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(movies[position])
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
                itemView.setOnClickListener { onItemClick.invoke(movie) }
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
