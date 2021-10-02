/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.ui.person.PersonMoviesFragment
import com.aastudio.sarollahi.moviebox.ui.person.PersonTVShowsFragment

internal class CreditsTabsAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    var movies: ArrayList<Movie>?,
    var shows: ArrayList<TVShow>?,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PersonMoviesFragment.newInstance(movies)
        }
        return PersonTVShowsFragment.newInstance(shows)
    }
}
