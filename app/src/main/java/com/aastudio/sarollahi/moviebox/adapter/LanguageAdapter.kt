/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Language
import com.aastudio.sarollahi.moviebox.databinding.RowTextBinding

class LanguageAdapter(
    private var language: MutableList<Language>,
    private val onItemClick: (language: Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = RowTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun getItemCount(): Int = language.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(language[position])
    }

    fun appendLanguage(language: List<Language>) {
        this.language.addAll(language)
        notifyItemRangeInserted(
            this.language.size,
            language.size - 1
        )
    }

    inner class LanguageViewHolder(itemView: RowTextBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var language = itemView.generalText

        fun bind(language: Language) {
            readyToReuse()
            this.language.text = language.name
            itemView.setOnClickListener { onItemClick.invoke(language) }
        }

        private fun readyToReuse() {
            this.language.text = ""
        }
    }
}
