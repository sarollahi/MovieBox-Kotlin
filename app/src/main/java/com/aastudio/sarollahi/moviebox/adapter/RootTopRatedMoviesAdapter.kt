package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.api.model.Movie
import com.bumptech.glide.Glide
import java.util.*

class RootTopRatedMoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RootTopRatedMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_movie_horizontal, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        private var title: TextView = itemView.findViewById(R.id.movieTitle)
        private var info: TextView = itemView.findViewById(R.id.movieInfo)

        fun bind(movie: Movie) {
            var infoText: String? = null
            if (!movie.posterPath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.posterPath}")
                    .into(poster)
                if (!movie.releaseDate.isNullOrEmpty()) {
                    infoText = movie.releaseDate?.substring(0, 4)
                }
                if (!movie.originalLanguage.isNullOrEmpty() && infoText.isNullOrEmpty()) {
                    infoText = movie.originalLanguage?.toUpperCase(Locale.getDefault())
                } else if (!movie.originalLanguage.isNullOrEmpty() && !infoText.isNullOrEmpty()) {
                    infoText = "$infoText | ${movie.originalLanguage?.toUpperCase(Locale.getDefault())}"
                }
                if (infoText.isNullOrEmpty()) {
                    infoText = movie.rating.toString()
                } else {
                    infoText = "$infoText | ${movie.rating}"
                }
                title.text = movie.title
                info.text = infoText
                itemView.setOnClickListener { onMovieClick.invoke(movie) }
            }
        }
    }
}