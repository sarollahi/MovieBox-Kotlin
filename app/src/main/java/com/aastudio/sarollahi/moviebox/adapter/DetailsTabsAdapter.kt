/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.ui.movie.MovieInfoFragment
import com.aastudio.sarollahi.moviebox.ui.review.ReviewFragment

@Suppress("DEPRECATION")
internal class DetailsTabsAdapter(
    fm: FragmentManager,
    var totalTabs: Int,
    var movie: Movie
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MovieInfoFragment.newInstance(movie)
            }
            1 -> {
                ReviewFragment.newInstance(movie.reviews)
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
