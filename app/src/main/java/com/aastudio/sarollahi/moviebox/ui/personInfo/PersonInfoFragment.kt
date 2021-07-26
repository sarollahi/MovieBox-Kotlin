/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.personInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.databinding.FragmentPersonInfoBinding
import com.bumptech.glide.Glide
import com.uncopt.android.widget.text.justify.JustifiedTextView

class PersonInfoFragment : Fragment() {

    private var person: Person? = null
    lateinit var binding: FragmentPersonInfoBinding
    private var bio: JustifiedTextView? = null
    private var birthDay: TextView? = null
    private var actorPopularity: RatingBar? = null
    private var deathDay: TextView? = null
    private var gender: TextView? = null
    private var birthPlace: TextView? = null
    private var profileImage: ImageView? = null
    private var homePage: ImageView? = null
    private var imdb: ImageView? = null
    private var search: Button? = null

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
        // Inflate the layout for this fragment
        binding = FragmentPersonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()

        person?.apply {
            biography?.let {
                bio?.text = it
            }
            birthday?.let {
                birthDay?.text = it
            }
            deathday?.let {
            }
            gender?.let {
            }
            homepage?.let {
            }
            imdb_id?.let {
            }
            popularity?.let {
                actorPopularity?.rating = it
            }
            adult?.let {
            }
            profilePath?.let {
                profileImage?.let { profileImage ->
                    Glide.with(requireContext())
                        .load("$IMAGE_ADDRESS$it")
                        .into(profileImage)
                }
            }
            placeOfBirth?.let {
                birthPlace?.text = it
            }
        }
    }

    private fun setUpUI() {
        bio = binding.actorBio
        birthDay = binding.actorBirth
        birthPlace = binding.actorBirthPlace
        profileImage = binding.actorImage
        homePage = binding.actorHomePage
        imdb = binding.actorImdb
        search = binding.searchMTByActor
        actorPopularity = binding.actorPopularity
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
