/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowMovieHorizontalSmallBinding
import com.bumptech.glide.Glide

class MoviesHorizontalSmallAdapter(
    private var movies: MutableList<Movie>,
    private val onItemClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MoviesHorizontalSmallAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowMovieHorizontalSmallBinding.inflate(
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

    inner class MovieViewHolder(itemView: RowMovieHorizontalSmallBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster: ImageView = itemView.imagePoster
        private var title: TextView = itemView.textTitle
        private var rating: TextView = itemView.textRating

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                reuse()
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .into(poster)
                title.text = movie.title
                if (movie.rating != null) {
                    val rate = (movie.rating!! * 10).toInt()
                    rating.text = itemView.context.getString(R.string.rating, rate, "%")
                } else {
                    rating.text = "--"
                }
                itemView.setOnClickListener { onItemClick.invoke(movie) }
            }
        }

        private fun reuse() {
            title.text = ""
            rating.text = ""
        }
    }
}
