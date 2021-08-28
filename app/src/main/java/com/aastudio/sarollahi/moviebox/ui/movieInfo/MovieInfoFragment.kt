/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox.ui.movieInfo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Movie
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.CastAdapter
import com.aastudio.sarollahi.moviebox.adapter.CompanyAdapter
import com.aastudio.sarollahi.moviebox.adapter.CountryAdapter
import com.aastudio.sarollahi.moviebox.adapter.DirectorAdapter
import com.aastudio.sarollahi.moviebox.adapter.LanguageAdapter
import com.aastudio.sarollahi.moviebox.adapter.RecommendedMoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.RelatedMoviesAdapter
import com.aastudio.sarollahi.moviebox.adapter.WriterAdapter
import com.aastudio.sarollahi.moviebox.databinding.FragmentInfoBinding
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieViewModel
import com.aastudio.sarollahi.moviebox.ui.personDetails.PersonDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieInfoFragment : Fragment() {
    lateinit var binding: FragmentInfoBinding
    private val viewModel by viewModel<MovieViewModel>()
    private var movie: Movie? = null
    private val director = mutableListOf<Person>()
    private val writer = mutableListOf<Person>()
    private var arrowDown: Boolean = false
    private lateinit var castAdapter: CastAdapter
    private lateinit var writerAdapter: WriterAdapter
    private lateinit var directorAdapter: DirectorAdapter
    private lateinit var companyAdapter: CompanyAdapter
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var relatedAdapter: RelatedMoviesAdapter
    private lateinit var recommendedAdapter: RecommendedMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            movie = it.getParcelable(MOVIE_INFO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.castRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        castAdapter = CastAdapter(mutableListOf()) { person -> showPersonDetails(person) }
        binding.castRecyclerView.adapter = castAdapter

        binding.directorRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        directorAdapter = DirectorAdapter(mutableListOf()) { person -> showPersonDetails(person) }
        binding.directorRecyclerView.adapter = directorAdapter

        binding.writerRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        writerAdapter = WriterAdapter(mutableListOf()) { person -> showPersonDetails(person) }
        binding.writerRecyclerView.adapter = writerAdapter

        binding.movieCompany.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        companyAdapter = CompanyAdapter(mutableListOf()) {}
        binding.movieCompany.adapter = companyAdapter

        binding.movieCountry.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        countryAdapter = CountryAdapter(mutableListOf()) {}
        binding.movieCountry.adapter = countryAdapter

        binding.movieLanguage.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        languageAdapter = LanguageAdapter(mutableListOf()) {}
        binding.movieLanguage.adapter = languageAdapter

        binding.movieRelated.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        relatedAdapter = RelatedMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.movieRelated.adapter = relatedAdapter

        binding.movieRecommended.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recommendedAdapter =
            RecommendedMoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
        binding.movieRecommended.adapter = recommendedAdapter

        val extras = activity?.intent?.getLongExtra(MovieDetailsActivity.MOVIE_ID, 0L)
        if (extras != 0L) {
            if (extras != null) {
                viewModel.getMovieDetails(extras)
            }
        } else {
            activity?.finish()
        }

        movie?.apply {
            binding.movieOverview.apply {
                text = overview
                if (lineCount >= 5) {
                    setVisibility(binding.overviewArrow)
                }
            }

            credits?.castList?.let {
                if (it.isNotEmpty() && castAdapter.itemCount == 0) {
                    castAdapter.appendCast(it)
                    castAdapter.notifyDataSetChanged()
                }
            }

            budget?.let {
                if (it.isNotEmpty() && it != "0") {
                    setVisibility(binding.movieBudget)
                    binding.movieBudget.text = it
                }
            }

            language?.let {
                if (it.isNotEmpty() && languageAdapter.itemCount == 0) {
                    setVisibility(binding.movieLanguage)
                    languageAdapter.appendLanguage(it)
                    languageAdapter.notifyDataSetChanged()
                }
            }

            country?.let {
                if (it.isNotEmpty() && countryAdapter.itemCount == 0) {
                    setVisibility(binding.movieCountry)
                    countryAdapter.appendCountry(it)
                    countryAdapter.notifyDataSetChanged()
                }
            }

            company?.let {
                if (it.isNotEmpty() && companyAdapter.itemCount == 0) {
                    setVisibility(binding.movieCompany)
                    companyAdapter.appendCompany(it)
                    companyAdapter.notifyDataSetChanged()
                }
            }

            similar?.results?.let {
                if (it.isNotEmpty() && relatedAdapter.itemCount == 0) {
                    setVisibility(binding.movieRelated)
                    relatedAdapter.appendRelatedMovies(it)
                    relatedAdapter.notifyDataSetChanged()
                }
            }

            recommendations?.results?.let {
                if (it.isNotEmpty() && recommendedAdapter.itemCount == 0) {
                    setVisibility(binding.movieRecommended)
                    recommendedAdapter.appendRecommendedMovies(it)
                    recommendedAdapter.notifyDataSetChanged()
                }
            }

            credits?.crewList?.let {
                for (crew in it) {
                    if (crew.profilePath != null && crew.department == DIRECTOR) {
                        director.add(crew)
                    } else if (crew.profilePath != null && crew.department == WRITER) {
                        writer.add(crew)
                    }
                }
            }

            if (director.isNotEmpty() && directorAdapter.itemCount == 0) {
                setVisibility(binding.directorRecyclerView)
                directorAdapter.appendCrew(director)
                directorAdapter.notifyDataSetChanged()
            }

            if (writer.isNotEmpty() && writerAdapter.itemCount == 0) {
                setVisibility(binding.writerRecyclerView)
                writerAdapter.appendCrew(writer)
                writerAdapter.notifyDataSetChanged()
            }
        }

        binding.overviewArrow.setOnClickListener {
            overViewStatus(arrowDown)
        }
    }

    private fun setVisibility(view: View?) {
        when (view) {
            binding.movieBudget -> {
                binding.budgetTitle.visibility = View.VISIBLE
                binding.movieBudget.visibility = View.VISIBLE
            }
            binding.writerRecyclerView -> {
                binding.writerTitle.visibility = View.VISIBLE
                binding.writerRecyclerView.visibility = View.VISIBLE
            }
            binding.directorRecyclerView -> {
                binding.directorTitle.visibility = View.VISIBLE
                binding.directorRecyclerView.visibility = View.VISIBLE
            }
            binding.overviewArrow -> {
                binding.overviewArrow.visibility = View.VISIBLE
            }
            binding.movieLanguage -> {
                binding.languageTitle.visibility = View.VISIBLE
                binding.movieLanguage.visibility = View.VISIBLE
            }
            binding.movieCountry -> {
                binding.countryTitle.visibility = View.VISIBLE
                binding.movieCountry.visibility = View.VISIBLE
            }
            binding.movieCompany -> {
                binding.companyTitle.visibility = View.VISIBLE
                binding.movieCompany.visibility = View.VISIBLE
            }
            binding.movieRelated -> {
                binding.relatedTitle.visibility = View.VISIBLE
                binding.movieRelated.visibility = View.VISIBLE
            }
            binding.movieRecommended -> {
                binding.recommendedTitle.visibility = View.VISIBLE
                binding.movieRecommended.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun overViewStatus(down: Boolean) {
        if (down) {
            val animation = ObjectAnimator.ofInt(
                binding.movieOverview,
                MAX_LINE,
                25
            )
            animation.duration = 500
            animation.start()
            arrowDown = false
            binding.overviewArrow.setBackgroundResource(R.drawable.ic_arrow_up)
        } else {
            val animation = ObjectAnimator.ofInt(
                binding.movieOverview,
                MAX_LINE,
                4
            )
            animation.duration = 500
            animation.start()
            arrowDown = true
            binding.overviewArrow.setBackgroundResource(R.drawable.ic_arrow_down)
        }
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.id)
        startActivity(intent)
        activity?.finish()
    }

    private fun showPersonDetails(person: Person) {
        val intent = Intent(context, PersonDetailsActivity::class.java)
        intent.putExtra(PersonDetailsActivity.PERSON_ID, person.id)
        intent.putExtra(PersonDetailsActivity.PERSON_NAME, person.name)
        startActivity(intent)
    }

    companion object {
        const val MAX_LINE = "maxLines"
        private const val MOVIE_INFO = "movieInfo"
        const val DIRECTOR = "Directing"
        const val WRITER = "Writing"

        fun newInstance(movie: Movie): MovieInfoFragment {
            val fragment = MovieInfoFragment()
            val args = Bundle()
            args.putParcelable(MOVIE_INFO, movie)
            fragment.arguments = args
            return fragment
        }
    }
}
