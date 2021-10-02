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
import com.aastudio.sarollahi.moviebox.databinding.RowPersonBinding
import com.bumptech.glide.Glide

class PeopleAdapter(
    private var people: MutableList<Person>,
    private val onItemClick: (person: Person) -> Unit
) : RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = RowPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int = people.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(people[position])
    }

    fun appendPeople(people: List<Person>) {
        this.people.addAll(people)
        notifyItemRangeInserted(
            this.people.size,
            people.size - 1
        )
    }

    inner class PersonViewHolder(itemView: RowPersonBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val profilePath = itemView.profileImage
        private var name = itemView.name

        fun bind(person: Person) {
            reuse()
            Glide.with(itemView)
                .load("$IMAGE_ADDRESS${person.profilePath}")
                .fitCenter()
                .into(profilePath)
            name.text = person.name
            itemView.setOnClickListener { onItemClick.invoke(person) }
        }

        fun reuse() {
            name.text = ""
            profilePath.setImageResource(0)
        }
    }
}
