/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.views.presenters

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.aastudio.sarollahi.api.model.Movie

class MovieDetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(
        viewHolder: ViewHolder,
        item: Any
    ) {
        val movie =
            item as? Movie
        if (movie != null) {
            viewHolder.title.text = movie.title
            viewHolder.subtitle.text = movie.releaseDate
            viewHolder.body.text = movie.overview
        }
    }
}
