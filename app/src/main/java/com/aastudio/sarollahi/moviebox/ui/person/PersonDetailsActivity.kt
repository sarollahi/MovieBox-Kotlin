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
import com.aastudio.sarollahi.moviebox.util.CustomViewPager
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<PersonViewModel>()
    lateinit var binding: ActivityPersonDetailsBinding
    private var tabLayout: TabLayout? = null
    private var viewPager: CustomViewPager? = null

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
                tabLayout?.newTab()?.setText(R.string.biography)?.let { tabLayout?.addTab(it) }
                tabLayout?.newTab()?.setText(R.string.images)?.let { tabLayout?.addTab(it) }
                val adapter = tabLayout?.tabCount?.let {
                    PersonTabsAdapter(
                        this@PersonDetailsActivity,
                        supportFragmentManager,
                        it,
                        person
                    )
                }
                viewPager?.adapter = adapter
                viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
                tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        viewPager?.currentItem = tab.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {}
                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setUpUI() {
        tabLayout = binding.detailTabs
        viewPager = binding.personDetailViewPager
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
