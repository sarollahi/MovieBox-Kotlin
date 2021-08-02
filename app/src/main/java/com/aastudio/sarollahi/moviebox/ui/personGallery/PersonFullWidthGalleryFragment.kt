/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.personGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.adapter.GalleryAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentPersonFullWidthGalleryBinding

class PersonFullWidthGalleryFragment : Fragment() {
    lateinit var binding: FragmentPersonFullWidthGalleryBinding
    private var person: Person? = null
    private var position: Int? = null
    private var personGallery: RecyclerView? = null
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            person = it.getParcelable(PERSON_GALLERY)
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonFullWidthGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()

        person?.apply {
            images?.let {
                it.profiles?.let { list ->
                    galleryAdapter.appendImages(list)
                    galleryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setUpUI() {
        personGallery = binding.gallery

        personGallery?.setHasFixedSize(true)
        personGallery?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        galleryAdapter = GalleryAdapter(mutableListOf()) { _, _ -> }
        personGallery?.adapter = galleryAdapter
        position?.let { personGallery?.findViewHolderForAdapterPosition(it)?.itemView?.performClick() }
    }

    companion object {
        private const val PERSON_GALLERY = "personGallery"
        private const val POSITION = "position"

        fun newInstance(person: Person, position: Int): PersonGalleryFragment {
            val fragment = PersonGalleryFragment()
            val args = Bundle()
            args.putParcelable(PERSON_GALLERY, person)
            args.putInt(POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}
