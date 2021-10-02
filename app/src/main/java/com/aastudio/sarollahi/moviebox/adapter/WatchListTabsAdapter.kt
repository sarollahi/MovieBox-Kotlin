/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aastudio.sarollahi.api.model.WatchList
import com.aastudio.sarollahi.moviebox.ui.watchList.WatchListMoviesFragment
import com.aastudio.sarollahi.moviebox.ui.watchList.WatchListTVShowsFragment

internal class WatchListTabsAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    var movies: ArrayList<WatchList>?,
    var shows: ArrayList<WatchList>?,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return WatchListMoviesFragment.newInstance(movies)
        }
        return WatchListTVShowsFragment.newInstance(shows)
    }
}
