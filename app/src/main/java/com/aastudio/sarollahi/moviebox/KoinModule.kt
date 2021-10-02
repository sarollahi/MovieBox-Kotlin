/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox

import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.MovieViewModel
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.NowPlayingMoviesViewModel
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.PopularMoviesViewModel
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.RootMovieViewModel
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.TopRatedMoviesViewModel
import com.aastudio.sarollahi.moviebox.ui.movie.viewModel.UpcomingMoviesViewModel
import com.aastudio.sarollahi.moviebox.ui.person.viewModel.CreditsViewModel
import com.aastudio.sarollahi.moviebox.ui.person.viewModel.PersonViewModel
import com.aastudio.sarollahi.moviebox.ui.search.viewModel.SearchViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.NowPlayingTVViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.PopularTVViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.RootTVViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.TVSeasonViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.TVViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.TopRatedTVViewModel
import com.aastudio.sarollahi.moviebox.ui.tv.viewModel.UpcomingTVViewModel
import com.aastudio.sarollahi.moviebox.ui.watchList.viewModel.WatchListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MovieViewModel(androidApplication()) }
    viewModel { TVViewModel(androidApplication()) }
    viewModel { NowPlayingMoviesViewModel(androidApplication()) }
    viewModel { TopRatedMoviesViewModel(androidApplication()) }
    viewModel { UpcomingMoviesViewModel(androidApplication()) }
    viewModel { PopularMoviesViewModel(androidApplication()) }
    viewModel { RootMovieViewModel(androidApplication()) }
    viewModel { RootTVViewModel(androidApplication()) }
    viewModel { UpcomingTVViewModel(androidApplication()) }
    viewModel { PopularTVViewModel(androidApplication()) }
    viewModel { TopRatedTVViewModel(androidApplication()) }
    viewModel { NowPlayingTVViewModel(androidApplication()) }
    viewModel { TVSeasonViewModel(androidApplication()) }
    viewModel { SearchViewModel(androidApplication()) }
    viewModel { PersonViewModel(androidApplication()) }
    viewModel { CreditsViewModel(androidApplication()) }
    viewModel { WatchListViewModel(androidApplication()) }
}
