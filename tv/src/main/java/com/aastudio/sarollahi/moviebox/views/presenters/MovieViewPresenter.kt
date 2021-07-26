package com.aastudio.sarollahi.moviebox.views.presenters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieViewPresenter(private val context: Context) : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        return CardViewHolder(ImageCardView(context))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        (item as? Movie)?.let { movie ->
            Log.d("iamhere1", movie.posterPath.toString())
            (viewHolder as? CardViewHolder)?.imageCardView?.apply {
                titleText = movie.title
                contentText = movie.overview
                Glide.with(context).load("https://image.tmdb.org/t/p/original${movie.posterPath}")
                    .apply(RequestOptions().centerCrop())
                    .into(this.mainImageView)
            }
        }
    }


    class CardViewHolder(itemView: View) : ViewHolder(itemView) {
        var imageCardView: ImageCardView = itemView as ImageCardView

        init {
            imageCardView.apply {
                isFocusable = true
                cardType = BaseCardView.CARD_TYPE_MAIN_ONLY
                infoVisibility = BaseCardView.CARD_REGION_VISIBLE_ALWAYS
                setMainImageDimensions(200, 300)
                badgeImage =
                    ContextCompat.getDrawable(context, R.drawable.ic_play_circle_outline_black_24dp)
            }
        }
    }


    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }
}