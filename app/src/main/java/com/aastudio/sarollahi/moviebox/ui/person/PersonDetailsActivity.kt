/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.PersonTabsAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityPersonDetailsBinding
import com.aastudio.sarollahi.moviebox.ui.person.viewModel.PersonViewModel
import com.aastudio.sarollahi.moviebox.util.ViewPager2ViewHeightAnimator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<PersonViewModel>()
    lateinit var binding: ActivityPersonDetailsBinding
    private var tabLayout: TabLayout? = null

    companion object {
        const val PERSON_ID = "person_id"
        const val PERSON_NAME = "person_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(PERSON_NAME) ?: getString(R.string.app_name)

        setUpUI()

        val extras = intent.getIntExtra(PERSON_ID, 0)
        if (extras != 0) {
            viewModel.getPersonDetails(extras)
        } else {
            finish()
        }

        viewModel.apply {
            observe(personInfo) { person ->
                val tabArrayList =
                    arrayListOf(
                        getString(R.string.biography),
                        getString(R.string.images)
                    )
                val adapter = PersonTabsAdapter(
                    supportFragmentManager,
                    2,
                    person,
                    lifecycle
                )
                binding.viewPager.adapter = adapter

                TabLayoutMediator(binding.detailTabs, binding.viewPager) { tab, position ->
                    tab.text = tabArrayList[position]
                }.attach()

                val fixPager2ViewHeightAnimator = ViewPager2ViewHeightAnimator()
                fixPager2ViewHeightAnimator.viewPager2 = binding.viewPager
                fixPager2ViewHeightAnimator.recalculate(binding.detailTabs.selectedTabPosition)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setUpUI() {
        tabLayout = binding.detailTabs
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
