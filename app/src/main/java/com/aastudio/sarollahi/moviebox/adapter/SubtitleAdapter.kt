/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.moviebox.databinding.RowSubtitleBinding
import com.masterwok.opensubtitlesandroid.models.OpenSubtitleItem

class SubtitleAdapter(
    private val subtitleList: Array<OpenSubtitleItem>,
    private val onItemClick: (item: OpenSubtitleItem) -> Unit
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val binding = RowSubtitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubtitleViewHolder(binding)
    }

    override fun getItemCount(): Int = subtitleList.size

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        holder.bind(subtitleList[position])
    }

    inner class SubtitleViewHolder(itemView: RowSubtitleBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var movieSubName = itemView.movieSubName
        private var movieSubLanguage = itemView.movieSubLanguage

        fun bind(item: OpenSubtitleItem) = with(itemView) {
            reuse()
            movieSubName.text = item.SubFileName
            movieSubLanguage.text = item.LanguageName
            setOnClickListener {
                itemView.setOnClickListener { onItemClick.invoke(item) }
            }
        }

        private fun reuse() {
            movieSubName.text = ""
            movieSubLanguage.text = ""
        }
    }
}
