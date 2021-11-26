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
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.TVShowsAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentVerticalRecyclerViewBinding
import com.aastudio.sarollahi.moviebox.ui.tv.TVDetailsActivity
import com.facebook.ads.AudienceNetworkAds
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.SdkInitializationListener
import com.mopub.nativeads.FacebookAdRenderer
import com.mopub.nativeads.MoPubRecyclerAdapter
import com.mopub.nativeads.MoPubStaticNativeAdRenderer
import com.mopub.nativeads.ViewBinder

class PersonTVShowsFragment : Fragment() {
    lateinit var binding: FragmentVerticalRecyclerViewBinding
    private var tvShows: ArrayList<TVShow>? = null
    private lateinit var adapter: TVShowsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            tvShows = it.getParcelableArrayList(SHOW_LIST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerticalRecyclerViewBinding.inflate(inflater, container, false)

        AudienceNetworkAds.initialize(requireContext())
        val sdkOnFiguration = SdkConfiguration.Builder("")
        MoPub.initializeSdk(requireContext(), sdkOnFiguration.build(), initSdkListener())

        layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerView.layoutManager = layoutManager

        adapter =
            TVShowsAdapter(
                mutableListOf()
            ) { show -> showTVDetails(show) }

        tvShows?.apply {
            adapter.appendMovies(this)
        }

        setUpRecyclerView(adapter)

        return binding.root
    }

    private fun initSdkListener(): SdkInitializationListener {
        return SdkInitializationListener { }
    }

    private fun showTVDetails(show: TVShow) {
        val intent = Intent(context, TVDetailsActivity::class.java)
        intent.putExtra(TVDetailsActivity.SHOW_ID, show.id)
        startActivity(intent)
    }

    private fun setUpRecyclerView(adapter: TVShowsAdapter) {
        // Pass the recycler Adapter your original adapter.
        val myMoPubAdapter = MoPubRecyclerAdapter(requireActivity(), adapter)
        // Create an ad renderer and view binder that describe your native ad layout.
        val myViewBinder = ViewBinder.Builder(R.layout.mopub_native_ads_unit)
            .titleId(R.id.mopub_native_ad_title)
            .textId(R.id.mopub_native_ad_text)
            .mainImageId(R.id.mopub_native_ad_main_imageview)
            .iconImageId(R.id.mopub_native_ad_icon)
            .callToActionId(R.id.mopub_native_ad_cta)
            .privacyInformationIconImageId(R.id.mopub_native_ad_privacy)
            .sponsoredTextId(R.id.mopub_ad_sponsored_label)
            .build()

        val myRenderer = MoPubStaticNativeAdRenderer(myViewBinder)

        myMoPubAdapter.registerAdRenderer(myRenderer)

        val facebookAdRenderer = FacebookAdRenderer(
            FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.facebook_native_ads_unit)
                .titleId(R.id.native_ad_title)
                .textId(R.id.native_ad_body)
                .mediaViewId(R.id.native_ad_media)
                .adIconViewId(R.id.native_ad_icon)
                .adChoicesRelativeLayoutId(R.id.ad_choices_container)
                .advertiserNameId(R.id.native_ad_sponsored_label)
                .callToActionId(R.id.native_ad_call_to_action)
                .build()
        )
        myMoPubAdapter.registerAdRenderer(facebookAdRenderer)

        // Set up the RecyclerView and start loading ads
        binding.recyclerView.adapter = myMoPubAdapter
        myMoPubAdapter.loadAds(System.getenv("ADS_PLACEMENT_ID") ?: "")
    }

    companion object {
        private const val SHOW_LIST = "showList"

        fun newInstance(showList: ArrayList<TVShow>?): PersonTVShowsFragment {
            val fragment = PersonTVShowsFragment()
            val args = Bundle()
            args.putParcelableArrayList(SHOW_LIST, showList)
            fragment.arguments = args
            return fragment
        }
    }
}
