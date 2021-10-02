/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.CreditsTabsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityWatchListBinding
import com.aastudio.sarollahi.moviebox.ui.person.viewModel.CreditsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonCreditsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchListBinding
    private val viewModel by viewModel<CreditsViewModel>()

    companion object {
        const val ID = "id"
        const val NAME = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(NAME) ?: getString(R.string.app_name)

        val id = intent.getIntExtra(ID, -1)

        if (id != -1) {
            viewModel.getPersonCredits(id)
        } else {
            finish()
        }

        val tabArrayList =
            arrayListOf(getString(R.string.movies), getString(R.string.tv_shows))

        viewModel.apply {
            observe(creditsList) { result ->
                binding.loading.visibility = View.GONE
                val adapter = CreditsTabsAdapter(
                    supportFragmentManager,
                    2,
                    result?.get("movie") as ArrayList<Movie>?,
                    result?.get("show") as ArrayList<TVShow>?,
                    lifecycle
                )
                binding.viewPager.adapter = adapter
                binding.viewPager.isUserInputEnabled = false

                TabLayoutMediator(binding.watchListTab, binding.viewPager) { tab, position ->
                    tab.text = tabArrayList[position]
                }.attach()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
