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
import com.aastudio.sarollahi.moviebox.databinding.RowPosterBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class RelatedMoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onItemClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RelatedMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendRelatedMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: RowPosterBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.poster

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .transform(CenterCrop())
                    .into(poster)
                itemView.setOnClickListener { onItemClick.invoke(movie) }
            }
        }
    }
}
