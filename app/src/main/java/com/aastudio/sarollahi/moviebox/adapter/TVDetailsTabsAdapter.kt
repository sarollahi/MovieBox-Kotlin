/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.ui.review.ReviewFragment
import com.aastudio.sarollahi.moviebox.ui.tv.TVInfoFragment

internal class TVDetailsTabsAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    var show: TVShow,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TVInfoFragment.newInstance(show)
        }
        return ReviewFragment.newInstance(show.reviews)
    }
}
