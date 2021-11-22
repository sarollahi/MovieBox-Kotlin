/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Season
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowCreditsBinding
import com.bumptech.glide.Glide

class SeasonAdapter(
    private var season: MutableList<Season>,
    private var showPoster: String?,
    private val onItemClick: (season: Season) -> Unit
) : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val binding = RowCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonViewHolder(binding)
    }

    override fun getItemCount(): Int = season.size

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(season[position])
    }

    fun appendSeason(season: List<Season>, showPoster: String?) {
        this.season.addAll(season)
        this.showPoster = showPoster
        notifyItemRangeInserted(
            this.season.size,
            season.size - 1
        )
    }

    inner class SeasonViewHolder(itemView: RowCreditsBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val profilePath = itemView.profileImage
        private var name = itemView.name
        private var episodes = itemView.character

        fun bind(season: Season) {
            reuse()
            if (season.posterPath != null) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${season.posterPath}")
                    .fitCenter()
                    .into(profilePath)
            } else {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS$showPoster")
                    .fitCenter()
                    .into(profilePath)
            }
            episodes.text = itemView.context.getString(R.string.episodes, season.episodeCount.toString())
            name.text = season.name
            itemView.setOnClickListener { onItemClick.invoke(season) }
        }

        fun reuse() {
            name.text = ""
            episodes.text = ""
            profilePath.setImageResource(0)
        }
    }
}
