/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Review
import com.aastudio.sarollahi.api.model.Reviews
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.ReviewAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private var reviews: Reviews? = null
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewLayoutMgr: LinearLayoutManager
    private lateinit var binding: FragmentReviewBinding
    private val noReview = listOf(Review("No Reviews Found!", "unfortunately we couldn't find any review in our database!", null, null))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            reviews = it.getParcelable(REVIEWS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setUpUI()
        binding.reviewsRecyclerView.layoutManager = reviewLayoutMgr
        reviewAdapter = ReviewAdapter(mutableListOf())
        binding.reviewsRecyclerView.adapter = reviewAdapter

        reviews?.results?.let {
            if (it.isNotEmpty()) {
                reviewAdapter.appendReview(it)
                reviewAdapter.notifyDataSetChanged()
            } else {
                reviewAdapter.appendReview(noReview, true, context?.resources?.getDrawable(R.drawable.ic_error))
                reviewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setUpUI() {
        reviewLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    companion object {
        private const val REVIEWS = "reviews"

        fun newInstance(reviews: Reviews?): ReviewFragment {
            val fragment = ReviewFragment()
            val args = Bundle()
            args.putParcelable(REVIEWS, reviews)
            fragment.arguments = args
            return fragment
        }
    }
}
