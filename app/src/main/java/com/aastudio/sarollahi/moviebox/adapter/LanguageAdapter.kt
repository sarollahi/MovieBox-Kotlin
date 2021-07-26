package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.api.model.Language

class LanguageAdapter(
    private var language: MutableList<Language>,
    private val onCastClick: (language: Language) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_text, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = language.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(language[position])
    }

    fun appendLanguage(language: List<Language>) {
        this.language.addAll(language)
        notifyItemRangeInserted(
            this.language.size,
            language.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.generalText)

        fun bind(language: Language) {
            readyToReuse()
            name.text = language.name
            itemView.setOnClickListener { onCastClick.invoke(language) }
        }

        private fun readyToReuse() {
            name.text = ""
        }
    }
}