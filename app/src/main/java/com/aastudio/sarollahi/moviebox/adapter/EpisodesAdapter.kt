/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Episode
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.moviebox.databinding.RowEpisodeBinding
import com.bumptech.glide.Glide

class EpisodesAdapter(
    private var movies: List<Episode>,
    private val onItemClick: (episode: Episode) -> Unit
) : RecyclerView.Adapter<EpisodesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodesAdapter.MovieViewHolder {
        val binding =
            RowEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    inner class MovieViewHolder(itemView: RowEpisodeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.poster
        private var title = itemView.title
        private var release = itemView.release
        private var overview = itemView.overview
        private var ratingNumber = itemView.rating

        fun bind(episode: Episode) {
            if (!episode.stillPath.isNullOrEmpty()) {
                reuse()
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${episode.stillPath}")
                    .into(poster)
                release.text = if (episode.airDate == "" || episode.airDate.isNullOrEmpty()) {
                    episode.episodeNumber.toString()
                } else {
                    episode.airDate?.let {
                        "Episode ${episode.episodeNumber} | ${it.substring(0, 4)}"
                    }
                }
                title.text = episode.name ?: ""
                overview.text = episode.overview ?: ""
                ratingNumber.text = episode.rating.toString()
                itemView.setOnClickListener { onItemClick.invoke(episode) }
            }
        }

        private fun reuse() {
            title.text = ""
            release.text = ""
            overview.text = ""
            ratingNumber.text = ""
        }
    }
}
