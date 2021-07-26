package com.aastudio.sarollahi.moviebox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.api.model.Movie
import com.bumptech.glide.Glide

class RootUpcommingMoviesAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<RootUpcommingMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_upcomming_movie_horizontal, parent, false)
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

        fun bind(movie: Movie) {
            if (!movie.posterPath.isNullOrEmpty()) {
                Glide.with(itemView)
                    .load("$IMAGE_ADDRESS${movie.backdropPath}")
                    .fitCenter()
                    .into(poster)
                itemView.setOnClickListener { onMovieClick.invoke(movie) }
            }
        }
    }
}