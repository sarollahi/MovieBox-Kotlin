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
import com.aastudio.sarollahi.api.model.WatchList
import com.aastudio.sarollahi.moviebox.databinding.RowMovieVerticalBinding
import com.bumptech.glide.Glide
import java.util.Locale

class WatchListMoviesAdapter(
    private val movieList: ArrayList<WatchList>,
    private val onItemClick: (item: WatchList) -> Unit
) :
    RecyclerView.Adapter<WatchListMoviesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            RowMovieVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = movieList[position]

        reuse(holder)
        holder.movieOverview.text = currentItem.overview
        holder.movieRating.text = currentItem.rating
        holder.movieRelease.text =
            if (currentItem.releaseDate == "" || currentItem.releaseDate.isNullOrEmpty()) {
                currentItem.originalLanguage
            } else {
                currentItem.releaseDate?.let {
                    "${it.substring(0, 4)} | ${
                    currentItem.originalLanguage?.uppercase(
                        Locale.getDefault()
                    )
                    }"
                }
            }
        holder.movieTitle.text = currentItem.title
        Glide.with(holder.itemView)
            .load("$IMAGE_ADDRESS${currentItem.posterPath}")
            .fitCenter()
            .into(holder.poster)
        holder.itemView.setOnClickListener { onItemClick.invoke(currentItem) }
    }

    private fun reuse(holder: MyViewHolder) {
        holder.movieOverview.text = ""
        holder.movieRating.text = ""
        holder.movieRelease.text = ""
        holder.movieTitle.text = ""
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MyViewHolder(itemView: RowMovieVerticalBinding) : RecyclerView.ViewHolder(itemView.root) {

        val movieOverview: TextView = itemView.movieOverview
        val movieRating: TextView = itemView.movieRating
        val movieRelease: TextView = itemView.movieRelease
        val movieTitle: TextView = itemView.movieTitle
        val poster: ImageView = itemView.moviePoster
    }
}
