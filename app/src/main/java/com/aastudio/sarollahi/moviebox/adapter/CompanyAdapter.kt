package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.api.model.Company

class CompanyAdapter(
    private var company: MutableList<Company>,
    private val onCastClick: (company: Company) -> Unit
) : RecyclerView.Adapter<CompanyAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_text, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = company.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(company[position])
    }

    fun appendCompany(company: List<Company>) {
        this.company.addAll(company)
        notifyItemRangeInserted(
            this.company.size,
            company.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView = itemView.findViewById(R.id.generalText)

        fun bind(company: Company) {
            name.text = company.name
            itemView.setOnClickListener { onCastClick.invoke(company) }
        }
    }
}