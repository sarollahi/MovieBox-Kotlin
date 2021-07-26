/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Images
import com.aastudio.sarollahi.moviebox.databinding.RowFullWidthImageBinding
import com.bumptech.glide.Glide

class FullWidthGalleryAdapter(
    private var gallery: MutableList<Images>,
) : RecyclerView.Adapter<FullWidthGalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemBinding =
            RowFullWidthImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = gallery.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(gallery[position])
    }

    fun appendImages(gallery: List<Images>) {
        this.gallery.addAll(gallery)
        notifyItemRangeInserted(
            this.gallery.size,
            gallery.size - 1
        )
    }

    inner class GalleryViewHolder(itemBinding: RowFullWidthImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val profilePath: ImageView = itemBinding.image

        fun bind(image: Images) {
            if (!image.path.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${image.path}")
                    .fitCenter()
                    .into(profilePath)
            }
        }
    }
}
