/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.moviebox.databinding.RowTextBinding

class StringAdapter(
    private var list: MutableList<String>,
    private val onItemClick: (string: String) -> Unit
) : RecyclerView.Adapter<StringAdapter.StringViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val binding = RowTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StringViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun appendString(stringList: List<String>) {
        this.list.addAll(stringList)
        notifyItemRangeInserted(
            this.list.size,
            list.size - 1
        )
    }

    inner class StringViewHolder(itemView: RowTextBinding) : RecyclerView.ViewHolder(itemView.root) {
        private var string = itemView.generalText

        fun bind(string: String) {
            reuse()
            this.string.text = string
            itemView.setOnClickListener { onItemClick.invoke(string) }
        }

        private fun reuse() {
            this.string.text = ""
        }
    }
}
