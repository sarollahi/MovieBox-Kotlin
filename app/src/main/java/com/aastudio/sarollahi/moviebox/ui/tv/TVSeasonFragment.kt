/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.common.observe
import com.aastudio.sarollahi.moviebox.adapter.EpisodesAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentTvSeasonBinding
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.TVSeasonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TVSeasonFragment : Fragment() {
    private val viewModel by viewModel<TVSeasonViewModel>()
    lateinit var binding: FragmentTvSeasonBinding
    private var show: TVShow? = null
    private var seasonList = arrayListOf<String?>()
    private val seasonMap = hashMapOf<String?, Int?>()
    private var episodesAdapter: EpisodesAdapter? = null
    private var episodesLayoutMgr: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            show = it.getParcelable(SHOW_SEASON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvSeasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.episodeContainer.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        show?.season.apply {
            if (!this.isNullOrEmpty()) {
                for (season in this) {
                    seasonList.add(season.name)
                    seasonMap[season.name] = season.seasonNumber
                }
            }
            binding.seasonContainer.setItems(seasonList)
        }

        episodesLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.episodeContainer.layoutManager = episodesLayoutMgr

        episodesAdapter =
            EpisodesAdapter(
                mutableListOf()
            ) { }

        binding.seasonContainer.selectItemByIndex(0)
        binding.seasonContainer.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            viewModel.episodesList.value = null
            seasonMap[newItem]?.let { getEpisodes(it) }
        }
    }

    private fun getEpisodes(season: Int) {
        viewModel.apply {
            show?.id?.let {
                getEpisodes(it, season)
            }

            observe(episodesList) { list ->
                if (!list.isNullOrEmpty()) {
                    episodesAdapter = EpisodesAdapter(list) {}
                    episodesAdapter?.notifyItemRangeChanged(0, list.size - 1)
                }
            }
        }
        binding.episodeContainer.adapter = episodesAdapter
    }

    companion object {
        private const val SHOW_SEASON = "showSeason"

        fun newInstance(show: TVShow): TVSeasonFragment {
            val fragment = TVSeasonFragment()
            val args = Bundle()
            args.putParcelable(SHOW_SEASON, show)
            fragment.arguments = args
            return fragment
        }
    }
}
