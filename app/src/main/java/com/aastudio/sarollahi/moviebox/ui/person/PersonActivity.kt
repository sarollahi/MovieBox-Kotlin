/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.common.calculateNoOfColumns
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.PeopleAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityPersonBinding
import com.aastudio.sarollahi.moviebox.ui.person.viewModel.PersonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonBinding
    private val viewModel by viewModel<PersonViewModel>()
    private val people = mutableSetOf<Person>()
    private lateinit var peopleAdapter: PeopleAdapter

    companion object {
        const val NAME = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME) ?: ""

        assert(supportActionBar != null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(NAME) ?: getString(R.string.app_name)

        viewModel.searchPeople(name)

        setUpUI()

        viewModel.apply {
            observe(people) { people ->
                for (person in people) {
                    if (person.profilePath != null) {
                        this@PersonActivity.people.add(person)
                    }
                }

                if (this@PersonActivity.people.isNotEmpty() && peopleAdapter.itemCount == 0) {
                    peopleAdapter.appendPeople(this@PersonActivity.people.toList())
                    peopleAdapter.notifyItemRangeChanged(0, people.size - 1)
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

    private fun setUpUI() {
        val mNoOfColumns: Int = calculateNoOfColumns(this, 180f)
        binding.people.setHasFixedSize(true)
        binding.people.layoutManager = GridLayoutManager(this, mNoOfColumns)
        peopleAdapter = PeopleAdapter(mutableListOf()) { person -> showPersonDetails(person) }
        binding.people.adapter = peopleAdapter
    }

    private fun showPersonDetails(person: Person) {
        val intent = Intent(this, PersonDetailsActivity::class.java)
        intent.putExtra(PersonDetailsActivity.PERSON_ID, person.id)
        intent.putExtra(PersonDetailsActivity.PERSON_NAME, person.name)
        startActivity(intent)
    }
}
