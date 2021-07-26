/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Country
import com.aastudio.sarollahi.moviebox.R

class CountryAdapter(
    private var country: MutableList<Country>,
    private val onCastClick: (country: Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_text, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = country.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(country[position])
    }

    fun appendCountry(country: List<Country>) {
        this.country.addAll(country)
        notifyItemRangeInserted(
            this.country.size,
            country.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.generalText)

        fun bind(country: Country) {
            name.text = country.name
            itemView.setOnClickListener { onCastClick.invoke(country) }
        }
    }
}
