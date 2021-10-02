/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.ui.person.PersonGalleryFragment
import com.aastudio.sarollahi.moviebox.ui.person.PersonInfoFragment

internal class PersonTabsAdapter(
    fragmentManager: FragmentManager,
    private var totalTabs: Int,
    var person: Person,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PersonInfoFragment.newInstance(person)
        }
        return PersonGalleryFragment.newInstance(person)
    }
}
