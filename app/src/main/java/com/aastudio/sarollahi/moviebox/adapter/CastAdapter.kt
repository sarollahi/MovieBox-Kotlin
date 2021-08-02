/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.databinding.RowCastBinding
import com.bumptech.glide.Glide

class CastAdapter(
    private var cast: MutableList<Person>,
    private val onCastClick: (cast: Person) -> Unit
) : RecyclerView.Adapter<CastAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = RowCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int = cast.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    fun appendCast(person: List<Person>) {
        this.cast.addAll(person)
        notifyItemRangeInserted(
            this.cast.size,
            person.size - 1
        )
    }

    inner class PersonViewHolder(itemView: RowCastBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val profilePath = itemView.castImage
        private var name = itemView.castName
        private var character = itemView.castCharacter

        fun bind(cast: Person) {
            if (!cast.profilePath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${cast.profilePath}")
                    .fitCenter()
                    .into(profilePath)
                name.text = cast.name
                character.text = cast.character
                itemView.setOnClickListener { onCastClick.invoke(cast) }
            }
        }
    }
}
