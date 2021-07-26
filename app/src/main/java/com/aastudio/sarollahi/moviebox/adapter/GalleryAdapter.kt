package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Images
import com.aastudio.sarollahi.moviebox.R
import com.bumptech.glide.Glide

class GalleryAdapter(
    private var gallery: MutableList<Images>,
    private val onImageClick: (image: Images, position: Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_image, parent, false)
        return GalleryViewHolder(view)
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

    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePath: ImageView = itemView.findViewById(R.id.image)

        fun bind(image: Images) {
            if (!image.path.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${image.path}")
                    .fitCenter()
                    .into(profilePath)
                itemView.setOnClickListener { onImageClick.invoke(image, adapterPosition) }
            }
        }
    }
}