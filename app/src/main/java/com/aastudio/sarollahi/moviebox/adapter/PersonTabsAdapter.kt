/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.ui.person.PersonGalleryFragment
import com.aastudio.sarollahi.moviebox.ui.person.PersonInfoFragment

@Suppress("DEPRECATION")
internal class PersonTabsAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
    var personInfo: Person
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PersonInfoFragment.newInstance(personInfo)
            }
            1 -> {
                PersonGalleryFragment.newInstance(personInfo)
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
