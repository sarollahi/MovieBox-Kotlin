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
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowMovieHorizontalSmallBinding
import com.bumptech.glide.Glide

class TVHorizontalSmallAdapter(
    private var shows: MutableList<TVShow>,
    private val onItemClick: (show: TVShow) -> Unit
) : RecyclerView.Adapter<TVHorizontalSmallAdapter.TVShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val binding = RowMovieHorizontalSmallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TVShowViewHolder(binding)
    }

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    fun appendShows(shows: List<TVShow>) {
        this.shows.addAll(shows)
        notifyItemRangeInserted(
            this.shows.size,
            shows.size - 1
        )
    }

    inner class TVShowViewHolder(itemView: RowMovieHorizontalSmallBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster: ImageView = itemView.imagePoster
        private var title: TextView = itemView.textTitle
        private var rating: TextView = itemView.textRating

        fun bind(show: TVShow) {
            if (!show.posterPath.isNullOrEmpty()) {
                reuse()
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${show.posterPath}")
                    .into(poster)
                title.text = show.name
                if (show.rating != null) {
                    val rate = (show.rating!!.toInt() * 10)
                    rating.text = itemView.context.getString(R.string.rating, rate, "%")
                } else {
                    rating.text = "--"
                }
                itemView.setOnClickListener { onItemClick.invoke(show) }
            }
        }

        private fun reuse() {
            title.text = ""
            rating.text = ""
        }
    }
}
