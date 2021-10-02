/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.person

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.api.model.IMAGE_ADDRESS
import com.aastudio.sarollahi.api.model.Images
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.databinding.ActivityPersonFullWidthGalleryBinding
import com.aastudio.sarollahi.moviebox.util.OnSwipeTouchListener
import com.bumptech.glide.Glide

class PersonFullWidthGalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonFullWidthGalleryBinding
    private var person: Person? = null
    private var position: Int = 0
    private var imageList: List<Images> = emptyList()

    companion object {
        const val PERSON = "person"
        const val POSITION = "position"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonFullWidthGalleryBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        person = intent.getParcelableExtra(PERSON)
        position = intent.getIntExtra(POSITION, -1)

        person?.apply {
            images?.let {
                it.profiles?.let { list ->
                    imageList = list
                    if (position >= 0) {
                        Glide.with(this@PersonFullWidthGalleryActivity)
                            .load("$IMAGE_ADDRESS${list[position].path}")
                            .fitCenter()
                            .into(binding.gallery)
                    }
                }
            }
        }

        binding.gallery.setOnTouchListener(object : OnSwipeTouchListener(this) {
            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeLeft() {
                if (position < imageList.size - 1) {
                    Glide.with(this@PersonFullWidthGalleryActivity)
                        .load("$IMAGE_ADDRESS${imageList[position + 1].path}")
                        .fitCenter()
                        .into(binding.gallery)
                    position++
                } else {
                    position = 0
                    Glide.with(this@PersonFullWidthGalleryActivity)
                        .load("$IMAGE_ADDRESS${imageList[position].path}")
                        .fitCenter()
                        .into(binding.gallery)
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeRight() {
                if (position > 0) {
                    Glide.with(this@PersonFullWidthGalleryActivity)
                        .load("$IMAGE_ADDRESS${imageList[position - 1].path}")
                        .fitCenter()
                        .into(binding.gallery)
                    position--
                } else {
                    position = imageList.size - 1
                    Glide.with(this@PersonFullWidthGalleryActivity)
                        .load("$IMAGE_ADDRESS${imageList[position].path}")
                        .fitCenter()
                        .into(binding.gallery)
                }
            }
        })
    }
}
