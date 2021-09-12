package com.aastudio.sarollahi.moviebox.ui.tvInfo

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aastudio.sarollahi.api.model.Person
import com.aastudio.sarollahi.api.model.TVShow
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.adapter.*
import com.aastudio.sarollahi.moviebox.databinding.FragmentTvInfoBinding
import com.aastudio.sarollahi.moviebox.ui.personDetails.PersonDetailsActivity
import com.aastudio.sarollahi.moviebox.ui.tvDetails.TVDetailsActivity

class TVInfoFragment : Fragment() {
    lateinit var binding: FragmentTvInfoBinding
    private var show: TVShow? = null
    private val cast = mutableSetOf<Person>()
    private val director = mutableSetOf<Person>()
    private val writer = mutableSetOf<Person>()
    private var arrowDown: Boolean = false
    private lateinit var castAdapter: CastAdapter
    private lateinit var writerAdapter: WriterAdapter
    private lateinit var directorAdapter: DirectorAdapter
    private lateinit var companyAdapter: CompanyAdapter
    private lateinit var countryAdapter: StringAdapter
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var relatedAdapter: RelatedShowsAdapter
    private lateinit var recommendedAdapter: RecommendedShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            show = it.getParcelable(SHOW_INFO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTvInfoBinding.inflate(inflater, container, false)
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

        binding.showCompany.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        companyAdapter = CompanyAdapter(mutableListOf()) {}
        binding.showCompany.adapter = companyAdapter

        binding.showCountry.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        countryAdapter = StringAdapter(mutableListOf()) {}
        binding.showCountry.adapter = countryAdapter

        binding.showLanguage.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        languageAdapter = LanguageAdapter(mutableListOf()) {}
        binding.showLanguage.adapter = languageAdapter

        binding.relatedShow.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        relatedAdapter = RelatedShowsAdapter(mutableListOf()) { show -> showTVDetails(show) }
        binding.relatedShow.adapter = relatedAdapter

        binding.recommendedShow.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recommendedAdapter =
            RecommendedShowsAdapter(mutableListOf()) { show -> showTVDetails(show) }
        binding.recommendedShow.adapter = recommendedAdapter

        show?.apply {
            binding.showOverview.apply {
                if (!overview.isNullOrEmpty()){
                    setVisibility(binding.showOverview)
                    text = overview
                    if (lineCount >= 5) {
                        setVisibility(binding.overviewArrow)
                    }
                }
            }

            credits?.castList?.let {
                for (cast in it) {
                    if (cast.profilePath != null) {
                        this@TVInfoFragment.cast.add(cast)
                    }
                }
            }

            if (cast.isNotEmpty() && castAdapter.itemCount == 0) {
                setVisibility(binding.castRecyclerView)
                castAdapter.appendCast(cast.toList())
                castAdapter.notifyDataSetChanged()
            }

//            budget?.let {
//                if (it.isNotEmpty() && it != "0") {
//                    setVisibility(binding.movieBudget)
//                    binding.movieBudget.text = it
//                }
//            }

            language?.let {
                if (it.isNotEmpty() && languageAdapter.itemCount == 0) {
                    setVisibility(binding.showLanguage)
                    languageAdapter.appendLanguage(it)
                    languageAdapter.notifyDataSetChanged()
                }
            }

            country?.let {
                if (it.isNotEmpty() && countryAdapter.itemCount == 0) {
                    setVisibility(binding.showCountry)
                    countryAdapter.appendString(it)
                    countryAdapter.notifyDataSetChanged()
                }
            }

            company?.let {
                if (it.isNotEmpty() && companyAdapter.itemCount == 0) {
                    setVisibility(binding.showCompany)
                    companyAdapter.appendCompany(it)
                    companyAdapter.notifyDataSetChanged()
                }
            }

            similar?.results?.let {
                if (it.isNotEmpty() && relatedAdapter.itemCount == 0) {
                    setVisibility(binding.relatedShow)
                    relatedAdapter.appendRelatedShows(it)
                    relatedAdapter.notifyDataSetChanged()
                }
            }

            recommendations?.results?.let {
                if (it.isNotEmpty() && recommendedAdapter.itemCount == 0) {
                    setVisibility(binding.recommendedShow)
                    recommendedAdapter.appendRecommendedShows(it)
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
                directorAdapter.appendCrew(director.toList())
                directorAdapter.notifyDataSetChanged()
            }

            if (writer.isNotEmpty() && writerAdapter.itemCount == 0) {
                setVisibility(binding.writerRecyclerView)
                writerAdapter.appendCrew(writer.toList())
                writerAdapter.notifyDataSetChanged()
            }
        }

        binding.overviewArrow.setOnClickListener {
            overViewStatus(arrowDown)
        }
    }

    private fun setVisibility(view: View?) {
        when (view) {
            binding.showOverview -> {
                binding.OverviewTitle.visibility = View.VISIBLE
                binding.showOverview.visibility = View.VISIBLE
            }
            binding.castRecyclerView -> {
                binding.castTitle.visibility = View.VISIBLE
                binding.castRecyclerView.visibility = View.VISIBLE
            }
            binding.showBudget -> {
                binding.budgetTitle.visibility = View.VISIBLE
                binding.showBudget.visibility = View.VISIBLE
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
            binding.showLanguage -> {
                binding.languageTitle.visibility = View.VISIBLE
                binding.showLanguage.visibility = View.VISIBLE
            }
            binding.showCountry -> {
                binding.countryTitle.visibility = View.VISIBLE
                binding.showCountry.visibility = View.VISIBLE
            }
            binding.showCompany -> {
                binding.companyTitle.visibility = View.VISIBLE
                binding.showCompany.visibility = View.VISIBLE
            }
            binding.relatedShow -> {
                binding.relatedTitle.visibility = View.VISIBLE
                binding.relatedShow.visibility = View.VISIBLE
            }
            binding.recommendedShow -> {
                binding.recommendedTitle.visibility = View.VISIBLE
                binding.recommendedShow.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun overViewStatus(down: Boolean) {
        if (down) {
            val animation = ObjectAnimator.ofInt(
                binding.showOverview,
                MAX_LINE,
                25
            )
            animation.duration = 500
            animation.start()
            arrowDown = false
            binding.overviewArrow.setBackgroundResource(R.drawable.ic_arrow_up)
        } else {
            val animation = ObjectAnimator.ofInt(
                binding.showOverview,
                MAX_LINE,
                4
            )
            animation.duration = 500
            animation.start()
            arrowDown = true
            binding.overviewArrow.setBackgroundResource(R.drawable.ic_arrow_down)
        }
    }

    private fun showTVDetails(show: TVShow) {
        val intent = Intent(context, TVDetailsActivity::class.java)
        intent.putExtra(TVDetailsActivity.SHOW_ID, show.id)
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
        private const val SHOW_INFO = "showInfo"
        const val DIRECTOR = "Directing"
        const val WRITER = "Writing"

        fun newInstance(show: TVShow): TVInfoFragment {
            val fragment = TVInfoFragment()
            val args = Bundle()
            args.putParcelable(SHOW_INFO, show)
            fragment.arguments = args
            return fragment
        }
    }
}