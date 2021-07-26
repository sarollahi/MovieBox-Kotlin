package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Genre
import com.aastudio.sarollahi.moviebox.R

class GenreAdapter(
    private var genre: MutableList<Genre>,
    private val onCastClick: (genre: Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_text, parent, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int = genre.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genre[position])
    }

    fun appendGenre(genre: List<Genre>) {
        this.genre.addAll(genre)
        notifyItemRangeInserted(
            this.genre.size,
            genre.size - 1
        )
    }

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.generalText)

        fun bind(genre: Genre) {
            name.text = genre.name
            itemView.setOnClickListener { onCastClick.invoke(genre) }
        }
    }
}