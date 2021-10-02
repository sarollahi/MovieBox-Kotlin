/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.FragmentPersonInfoBinding
import com.bumptech.glide.Glide

class PersonInfoFragment : Fragment() {

    private var person: Person? = null
    lateinit var binding: FragmentPersonInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            person = it.getParcelable(PERSON_INFO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        person?.apply {
            biography?.let {
                binding.actorBio.text = it
            }
            birthday?.let {
                binding.actorBirth.text = it
            }
            deathday?.let {
                binding.actorDeath.text = getString(R.string.person_death_date, it)
            }
            homepage?.let { homePage ->
                binding.actorHomePage.visibility = View.VISIBLE
                binding.actorHomePage.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse(homePage)
                    startActivity(intent)
                }
            }
            imdb_id?.let { imdbId ->
                binding.actorImdb.visibility = View.VISIBLE
                binding.actorImdb.setOnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse(getString(R.string.imdb_url, imdbId))
                    startActivity(intent)
                }
            }
            popularity?.let {
                binding.actorPopularity.rating = it
            }
            profilePath?.let {
                Glide.with(requireContext())
                    .load("$IMAGE_ADDRESS$it")
                    .into(binding.actorImage)
            }
            placeOfBirth?.let {
                binding.actorBirthPlace.text = it
            }
        }

        binding.searchMTByActor.setOnClickListener {
            val intent = Intent(requireContext(), PersonCreditsActivity::class.java)
            intent.putExtra(PersonCreditsActivity.ID, person?.id)
            intent.putExtra(PersonCreditsActivity.NAME, person?.name)
            startActivity(intent)
        }
    }

    companion object {
        private const val PERSON_INFO = "personInfo"

        fun newInstance(person: Person): PersonInfoFragment {
            val fragment = PersonInfoFragment()
            val args = Bundle()
            args.putParcelable(PERSON_INFO, person)
            fragment.arguments = args
            return fragment
        }
    }
}
