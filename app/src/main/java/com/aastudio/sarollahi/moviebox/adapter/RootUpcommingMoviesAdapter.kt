/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowMovieHorizontalLargeBinding
import com.bumptech.glide.Glide
import java.util.Locale

class RootUpcommingMoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RootUpcommingMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowMovieHorizontalLargeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: RowMovieHorizontalLargeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster: ImageView = itemView.moviePoster
        private val title: TextView = itemView.textTitle
        private val vote: TextView = itemView.textVoteCount
        private val info: TextView = itemView.textLanguageAndRating
        private var rating: RatingBar = itemView.ratingBar

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                var infoText: String? = null
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .fitCenter()
                    .into(poster)
                title.text = movie.title
                movie.rating?.let {
                    rating.rating = it.div(2)
                }
                vote.text = movie.voteCount?.let {
                    itemView.context.resources.getQuantityString(
                        R.plurals.reviews,
                        it,
                        it
                    )
                } ?: ""
                if (!movie.originalLanguage.isNullOrEmpty() && infoText.isNullOrEmpty()) {
                    infoText = movie.originalLanguage?.uppercase(Locale.getDefault())
                } else if (!movie.originalLanguage.isNullOrEmpty() && !infoText.isNullOrEmpty()) {
                    infoText =
                        "$infoText | ${movie.originalLanguage?.uppercase(Locale.getDefault())}"
                }
                infoText = if (infoText.isNullOrEmpty()) {
                    movie.rating.toString()
                } else {
                    "$infoText | ${movie.rating}"
                }
                info.text = infoText
                itemView.setOnClickListener { onMovieClick.invoke(movie) }
            }
        }
    }
}
