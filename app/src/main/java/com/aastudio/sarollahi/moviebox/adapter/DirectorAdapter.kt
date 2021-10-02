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
import com.aastudio.sarollahi.moviebox.databinding.RowCreditsBinding
import com.bumptech.glide.Glide

class DirectorAdapter(
    private var crew: MutableList<Person>,
    private val onItemClick: (crew: Person) -> Unit
) : RecyclerView.Adapter<DirectorAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = RowCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int = crew.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(crew[position])
    }

    fun appendCrew(person: List<Person>) {
        this.crew.addAll(person)
        notifyItemRangeInserted(
            this.crew.size,
            person.size - 1
        )
    }

    inner class PersonViewHolder(itemView: RowCreditsBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val profilePath = itemView.profileImage
        private var name = itemView.name
        private var character = itemView.character

        fun bind(crew: Person) {
            reuse()
            character.visibility = View.GONE
            Glide.with(itemView)
                .load("$IMAGE_ADDRESS${crew.profilePath}")
                .fitCenter()
                .into(profilePath)
            name.text = crew.name
            itemView.setOnClickListener { onItemClick.invoke(crew) }
        }

        fun reuse() {
            name.text = ""
            profilePath.setImageResource(0)
        }
    }
}
