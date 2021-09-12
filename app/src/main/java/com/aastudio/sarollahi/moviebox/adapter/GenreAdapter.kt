/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.moviebox.databinding.RowTextBinding

class GenreAdapter(
    private var genre: MutableList<Genre>,
    private val onItemClick: (genre: Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = RowTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int = genre.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genre[position])
    }

    fun appendGenre(genre: List<Genre>) {
        this.genre.addAll(genre)
        notifyItemRangeInserted(
            this.genre.size,
            genre.size - 1
        )
    }

    inner class GenreViewHolder(itemView: RowTextBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var genre = itemView.generalText

        fun bind(genre: Genre) {
            reuse()
            this.genre.text = genre.name
            itemView.setOnClickListener { onItemClick.invoke(genre) }
        }

        private fun reuse() {
            this.genre.text = ""
        }
    }
}
