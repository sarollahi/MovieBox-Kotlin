/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Review
import com.aastudio.sarollahi.moviebox.databinding.RowReviewBinding

class ReviewAdapter(
    private var reviews: MutableList<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RowReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun appendReview(review: List<Review>) {
        this.reviews.addAll(review)
        notifyItemRangeInserted(
            this.reviews.size,
            review.size - 1
        )
    }

    inner class ReviewViewHolder(itemBinding: RowReviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private var authorName: TextView = itemBinding.authorName
        private var reviewText: TextView = itemBinding.reviewText

        fun bind(review: Review) {
            authorName.text = review.name
            reviewText.text = review.review
        }
    }
}
