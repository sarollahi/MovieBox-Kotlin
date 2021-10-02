/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.SearchTabsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityWatchListBinding
import com.aastudio.sarollahi.moviebox.ui.search.viewModel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchListBinding
    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        const val TITLE = "title"
        const val YEAR = "year"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val title = intent.getStringExtra(TITLE) ?: ""
        val year = intent.getStringExtra(YEAR) ?: ""

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(TITLE) ?: getString(R.string.app_name)

        viewModel.searchMovie(1, title, year)
        viewModel.searchTV(1, title, year)
        val tabArrayList =
            arrayListOf(getString(R.string.movies), getString(R.string.tv_shows))

        viewModel.apply {
            observe(result) { result ->

                val adapter = SearchTabsAdapter(
                    supportFragmentManager,
                    2,
                    result["movies"] as ArrayList<Movie>?,
                    result["shows"] as ArrayList<TVShow>?,
                    lifecycle
                )
                binding.viewPager.adapter = adapter
                binding.viewPager.isUserInputEnabled = false

                TabLayoutMediator(binding.watchListTab, binding.viewPager) { tab, position ->
                    tab.text = tabArrayList[position]
                }.attach()

                observe(loading) {
                    binding.loading.visibility = View.INVISIBLE
                }
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
