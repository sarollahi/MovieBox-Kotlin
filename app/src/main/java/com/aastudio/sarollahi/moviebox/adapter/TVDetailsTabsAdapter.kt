/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.ui.review.ReviewFragment
import com.aastudio.sarollahi.moviebox.ui.tv.TVInfoFragment
import com.aastudio.sarollahi.moviebox.ui.tv.TVSeasonFragment

@Suppress("DEPRECATION")
internal class TVDetailsTabsAdapter(
    fm: FragmentManager,
    var totalTabs: Int,
    var show: TVShow
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TVInfoFragment.newInstance(show)
            }
            1 -> {
                TVSeasonFragment.newInstance(show)
            }
            2 -> {
                ReviewFragment.newInstance(show.reviews)
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
