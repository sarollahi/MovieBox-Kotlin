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
import com.aastudio.sarollahi.moviebox.R
import com.masterwok.opensubtitlesandroid.models.OpenSubtitleItem

class SubtitleAdapter(
    private val subtitleList: Array<OpenSubtitleItem>,
    private val onCastClick: (item: OpenSubtitleItem) -> Unit
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_subtitle, parent, false)
        return SubtitleViewHolder(view)
    }

    override fun getItemCount(): Int = subtitleList.size

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        holder.bind(subtitleList[position])
    }

    inner class SubtitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var movieSubName: TextView = itemView.findViewById(R.id.movieSubName)
        private var movieSubLanguage: TextView = itemView.findViewById(R.id.movieSubLanguage)

        fun bind(item: OpenSubtitleItem) = with(itemView) {
            movieSubName.text = item.SubFileName
            movieSubLanguage.text = item.LanguageName
            setOnClickListener {
                itemView.setOnClickListener { onCastClick.invoke(item) }
            }
        }
    }
}
