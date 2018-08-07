/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rappitest.di

import com.example.rappitest.ui.movie.MovieFragment
import com.example.rappitest.ui.movieSearch.MovieSearchFragment
import com.example.rappitest.ui.tvShow.TvShowFragment
import com.example.rappitest.ui.tvShowSearch.TvShowSearchFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMovieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieSearchFragment(): MovieSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeTvShowFragment(): TvShowFragment

    @ContributesAndroidInjector
    abstract fun contributeTvShowSearchFragment(): TvShowSearchFragment
}
