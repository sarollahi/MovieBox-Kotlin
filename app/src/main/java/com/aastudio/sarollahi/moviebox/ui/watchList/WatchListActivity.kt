/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.watchList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.WatchListTabsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityWatchListBinding
import com.aastudio.sarollahi.moviebox.ui.watchList.viewModel.WatchListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class WatchListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchListBinding
    private val viewModel by viewModel<WatchListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchListBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.watchList)

        viewModel.getWatchList()
        val tabArrayList =
            arrayListOf(getString(R.string.movies), getString(R.string.tv_shows))

        viewModel.apply {
            observe(watchList) { watchlist ->

                val adapter = WatchListTabsAdapter(
                    supportFragmentManager,
                    2,
                    watchlist["movie"],
                    watchlist["show"],
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
