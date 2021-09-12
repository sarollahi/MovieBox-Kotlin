/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Company
import com.aastudio.sarollahi.moviebox.databinding.RowTextBinding

class CompanyAdapter(
    private var company: MutableList<Company>,
    private val onItemClick: (company: Company) -> Unit
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val binding = RowTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompanyViewHolder(binding)
    }

    override fun getItemCount(): Int = company.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(company[position])
    }

    fun appendCompany(company: List<Company>) {
        this.company.addAll(company)
        notifyItemRangeInserted(
            this.company.size,
            company.size - 1
        )
    }

    inner class CompanyViewHolder(itemView: RowTextBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var company = itemView.generalText

        fun bind(company: Company) {
            reuse()
            this.company.text = company.name
            itemView.setOnClickListener { onItemClick.invoke(company) }
        }

        private fun reuse() {
            this.company.text = ""
        }
    }
}
