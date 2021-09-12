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
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowMovieHorizontalLargeBinding
import com.bumptech.glide.Glide
import java.util.Locale

class TVHorizontalLargeAdapter(
    private var shows: MutableList<TVShow>,
    private val onItemClick: (show: TVShow) -> Unit
) : RecyclerView.Adapter<TVHorizontalLargeAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = RowMovieHorizontalLargeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    fun appendShows(shows: List<TVShow>) {
        this.shows.addAll(shows)
        notifyItemRangeInserted(
            this.shows.size,
            shows.size - 1
        )
    }

    inner class ShowViewHolder(itemView: RowMovieHorizontalLargeBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val poster = itemView.moviePoster
        private val title = itemView.textTitle
        private val vote = itemView.textVoteCount
        private val info = itemView.textLanguageAndRating
        private var rating = itemView.ratingBar

        fun bind(show: TVShow) {
            if (!show.posterPath.isNullOrEmpty()) {
                var infoText: String? = null
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${show.posterPath}")
                    .fitCenter()
                    .into(poster)
                title.text = show.name
                show.rating?.let {
                    rating.rating = it.div(2)
                }
                vote.text = show.voteCount?.let {
                    itemView.context.resources.getQuantityString(
                        R.plurals.reviews,
                        it,
                        it
                    )
                } ?: ""
                if (!show.originalLanguage.isNullOrEmpty() && infoText.isNullOrEmpty()) {
                    infoText = show.originalLanguage?.uppercase(Locale.getDefault())
                } else if (!show.originalLanguage.isNullOrEmpty() && !infoText.isNullOrEmpty()) {
                    infoText =
                        "$infoText | ${show.originalLanguage?.uppercase(Locale.getDefault())}"
                }
                infoText = if (infoText.isNullOrEmpty()) {
                    show.rating.toString()
                } else {
                    "$infoText | ${show.rating}"
                }
                info.text = infoText
                itemView.setOnClickListener { onItemClick.invoke(show) }
            }
        }
    }
}
