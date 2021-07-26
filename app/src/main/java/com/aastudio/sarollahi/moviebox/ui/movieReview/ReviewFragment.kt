/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movieReview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.moviebox.adapter.ReviewAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {
    private var movie: Movie? = null
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewLayoutMgr: LinearLayoutManager
    private lateinit var binding: FragmentReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            movie = it.getParcelable(MOVIE_REVIEW)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()
        binding.reviewsRecyclerView.layoutManager = reviewLayoutMgr
        reviewAdapter = ReviewAdapter(mutableListOf())
        binding.reviewsRecyclerView.adapter = reviewAdapter

        movie?.reviews?.results?.let {
            if (it.isNotEmpty()) {
                reviewAdapter.appendReview(it)
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
        private const val MOVIE_REVIEW = "movieReview"

        fun newInstance(review: Movie): ReviewFragment {
            val fragment = ReviewFragment()
            val args = Bundle()
            args.putParcelable(MOVIE_REVIEW, review)
            fragment.arguments = args
            return fragment
        }
    }
}
