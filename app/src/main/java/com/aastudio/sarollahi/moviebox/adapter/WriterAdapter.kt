/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.databinding.RowCastBinding
import com.bumptech.glide.Glide

class WriterAdapter(
    private var crew: MutableList<Person>,
    private val onCastClick: (crew: Person) -> Unit
) : RecyclerView.Adapter<WriterAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = RowCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = crew.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(crew[position])
    }

    fun appendCrew(person: List<Person>) {
        this.crew.addAll(person)
        notifyItemRangeInserted(
            this.crew.size,
            person.size - 1
        )
    }

    inner class MovieViewHolder(itemView: RowCastBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val profilePath = itemView.castImage
        private var name = itemView.castName
        private var character = itemView.castCharacter

        fun bind(crew: Person) {
            reuse()
            character.visibility = View.GONE
            Glide.with(itemView)
                .load("$IMAGE_ADDRESS${crew.profilePath}")
                .fitCenter()
                .into(profilePath)
            name.text = crew.name
            itemView.setOnClickListener { onCastClick.invoke(crew) }
        }

        private fun reuse() {
            name.text = ""
        }
    }
}
