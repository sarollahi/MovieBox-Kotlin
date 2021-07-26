package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.R
import com.bumptech.glide.Glide

class CastAdapter(
    private var cast: MutableList<Person>,
    private val onCastClick: (cast: Person) -> Unit
) : RecyclerView.Adapter<CastAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_cast, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = cast.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    fun appendCast(person: List<Person>) {
        this.cast.addAll(person)
        notifyItemRangeInserted(
            this.cast.size,
            person.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePath: ImageView = itemView.findViewById(R.id.castImage)
        private var name: TextView = itemView.findViewById(R.id.castName)
        private var character: TextView = itemView.findViewById(R.id.castCharacter)

        fun bind(cast: Person) {
            if (!cast.profilePath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${cast.profilePath}")
                    .fitCenter()
                    .into(profilePath)
                name.text = cast.name
                character.text = cast.character
                itemView.setOnClickListener { onCastClick.invoke(cast) }
            }
        }
    }
}