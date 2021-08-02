/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox

import com.aastudio.sarollahi.moviebox.ui.movieDetails.MovieViewModel
import com.aastudio.sarollahi.moviebox.ui.personDetails.PersonViewModel
import com.aastudio.sarollahi.moviebox.ui.rootMovie.RootMovieViewModel
import com.aastudio.sarollahi.moviebox.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MovieViewModel(androidApplication()) }
    viewModel { RootMovieViewModel(androidApplication()) }
    viewModel { SearchViewModel(androidApplication()) }
    viewModel { PersonViewModel(androidApplication()) }
}
