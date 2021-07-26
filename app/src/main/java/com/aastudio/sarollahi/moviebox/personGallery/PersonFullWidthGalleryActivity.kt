/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.personGallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.adapter.FullWidthGalleryAdapter
import com.aastudio.sarollahi.moviebox.databinding.ActivityPersonFullWidthGalleryBinding

class PersonFullWidthGalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonFullWidthGalleryBinding
    private var person: Person? = null
    private var position: Int? = null
    private var personGallery: RecyclerView? = null
    private var galleryLayoutMgr: LinearLayoutManager? = null
    private lateinit var galleryAdapter: FullWidthGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonFullWidthGalleryBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        person = intent.getParcelableExtra("person")
        position = intent.getIntExtra("pos", -1)

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
        galleryLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        personGallery?.layoutManager = galleryLayoutMgr

        galleryAdapter = FullWidthGalleryAdapter(mutableListOf())
        personGallery?.adapter = galleryAdapter
        position?.let {
            if (position != -1) {
                personGallery?.scrollToPosition(position!!)
            } else {
                finish()
            }
        }
    }
}
