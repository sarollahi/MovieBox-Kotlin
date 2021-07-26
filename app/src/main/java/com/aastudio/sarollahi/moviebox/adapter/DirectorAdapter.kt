/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.R
import com.bumptech.glide.Glide

class DirectorAdapter(
    private var crew: MutableList<Person>,
    private val onCastClick: (crew: Person) -> Unit
) : RecyclerView.Adapter<DirectorAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_cast, parent, false)
        return MovieViewHolder(view)
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

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePath: ImageView = itemView.findViewById(R.id.castImage)
        private var name: TextView = itemView.findViewById(R.id.castName)
        private var character: TextView = itemView.findViewById(R.id.castCharacter)

        fun bind(crew: Person) {
            character.visibility = View.GONE
            Glide.with(itemView)
                .load("$IMAGE_ADDRESS${crew.profilePath}")
                .fitCenter()
                .into(profilePath)
            name.text = crew.name
            itemView.setOnClickListener { onCastClick.invoke(crew) }
        }
    }
}
