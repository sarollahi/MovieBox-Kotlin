/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.common.calculateNoOfColumns
import com.aastudio.sarollahi.moviebox.adapter.GalleryAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentPersonGalleryBinding
import com.aastudio.sarollahi.moviebox.ui.person.PersonFullWidthGalleryActivity.Companion.PERSON
import com.aastudio.sarollahi.moviebox.ui.person.PersonFullWidthGalleryActivity.Companion.POSITION

class PersonGalleryFragment : Fragment() {

    lateinit var binding: FragmentPersonGalleryBinding
    private var person: Person? = null
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            person = it.getParcelable(PERSON_GALLERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()

        person?.apply {
            images?.let {
                it.profiles?.let { list ->
                    galleryAdapter.appendImages(list)
                    galleryAdapter.notifyItemRangeChanged(0, list.size - 1)
                }
            }
        }
    }

    private fun setUpUI() {
        val mNoOfColumns: Int = calculateNoOfColumns(requireContext(), 128f)
        binding.gallery.setHasFixedSize(true)
        binding.gallery.layoutManager = GridLayoutManager(activity, mNoOfColumns)
        galleryAdapter = GalleryAdapter(mutableListOf()) { _, position ->
            person?.let {
                val intent = Intent(requireActivity(), PersonFullWidthGalleryActivity::class.java)
                intent.putExtra(PERSON, it)
                intent.putExtra(POSITION, position)
                startActivity(intent)
            }
        }
        binding.gallery.adapter = galleryAdapter
    }

    companion object {
        private const val PERSON_GALLERY = "personGallery"

        fun newInstance(person: Person): PersonGalleryFragment {
            val fragment = PersonGalleryFragment()
            val args = Bundle()
            args.putParcelable(PERSON_GALLERY, person)
            fragment.arguments = args
            return fragment
        }
    }
}
