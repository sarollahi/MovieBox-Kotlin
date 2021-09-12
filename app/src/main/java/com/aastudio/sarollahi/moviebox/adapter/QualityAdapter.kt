/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.BaseMovie
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowMovieQualityBinding

class QualityAdapter(
    private var movie: MutableList<BaseMovie.Torrent>,
    private val onItemClick: (movie: BaseMovie.Torrent) -> Unit
) : RecyclerView.Adapter<QualityAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            RowMovieQualityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
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

    inner class MovieViewHolder(itemView: RowMovieQualityBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var quality = itemView.qualityText

        fun bind(movie: BaseMovie.Torrent) {
            reuse()
            quality.text = itemView.context.getString(R.string.quality, movie.type, movie.quality)
            itemView.setOnClickListener { onItemClick.invoke(movie) }
        }

        private fun reuse() {
            quality.text = ""
        }
    }
}
