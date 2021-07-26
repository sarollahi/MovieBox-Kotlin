/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.moviebox.R

class QualityAdapter(
    private var movie: MutableList<BaseMovie.Torrent>,
    private val onCastClick: (movie: BaseMovie.Torrent) -> Unit
) : RecyclerView.Adapter<QualityAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_movie_quality, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movie.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movie[position])
    }

    fun appendQuality(person: List<BaseMovie.Torrent>) {
        this.movie.addAll(person)
        notifyItemRangeInserted(
            this.movie.size,
            person.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var quality: TextView = itemView.findViewById(R.id.qualityText)

        fun bind(movie: BaseMovie.Torrent) {
            quality.text = "${movie.type} ${movie.quality}"
            itemView.setOnClickListener { onCastClick.invoke(movie) }
        }
    }
}
