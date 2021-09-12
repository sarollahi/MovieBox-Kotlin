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
import com.aastudio.sarollahi.moviebox.databinding.RowPosterBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class RelatedShowsAdapter(
    private var shows: MutableList<TVShow>,
    private val onItemClick: (show: TVShow) -> Unit
) : RecyclerView.Adapter<RelatedShowsAdapter.TVViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val binding = RowPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TVViewHolder(binding)
    }

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    fun appendRelatedShows(shows: List<TVShow>) {
        this.shows.addAll(shows)
        notifyItemRangeInserted(
            this.shows.size,
            shows.size - 1
        )
    }

    inner class TVViewHolder(itemView: RowPosterBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.poster

        fun bind(show: TVShow) {
            if (!show.posterPath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${show.posterPath}")
                    .transform(CenterCrop())
                    .into(poster)
                itemView.setOnClickListener { onItemClick.invoke(show) }
            }
        }
    }
}
