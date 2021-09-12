/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Country
import com.aastudio.sarollahi.moviebox.databinding.RowTextBinding

class CountryAdapter(
    private var country: MutableList<Country>,
    private val onItemClick: (country: Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = RowTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int = country.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(country[position])
    }

    fun appendCountry(country: List<Country>) {
        this.country.addAll(country)
        notifyItemRangeInserted(
            this.country.size,
            country.size - 1
        )
    }

    inner class CountryViewHolder(itemView: RowTextBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var country = itemView.generalText

        fun bind(country: Country) {
            reuse()
            this.country.text = country.name
            itemView.setOnClickListener { onItemClick.invoke(country) }
        }

        private fun reuse() {
            this.country.text = ""
        }
    }
}
