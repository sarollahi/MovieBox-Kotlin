/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Review
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.RowReviewBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

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
        private var reviewDate: TextView = itemBinding.reviewDate
        private var profileImage: ImageView = itemBinding.profileImage

        fun bind(review: Review) {
            reuse()
            authorName.text = review.name
            reviewText.text = review.review
            reviewDate.text = review.date?.let {
                itemView.context.getString(
                    R.string.onDate,
                    changeDateFormat(it)
                )
            } ?: ""
            Glide.with(itemView)
                .load("$IMAGE_ADDRESS${review.authorDetails?.avatarPath}")
                .circleCrop()
                .into(profileImage)
        }

        private fun reuse() {
            authorName.text = ""
            reviewText.text = ""
        }

        private fun changeDateFormat(strDate: String): String {
            val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val df = SimpleDateFormat(format, Locale.US)
            val date = df.parse(strDate)
            return android.text.format.DateFormat.format("MMM d, yyyy", date).toString()
        }
    }
}
