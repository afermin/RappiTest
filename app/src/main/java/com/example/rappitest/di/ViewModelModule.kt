package com.example.rappitest.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.rappitest.ui.movie.MovieViewModel
import com.example.rappitest.ui.movieSearch.MovieSearchViewModel
import com.example.rappitest.ui.tvShow.TvShowViewModel
import com.example.rappitest.ui.tvShowSearch.TvShowSearchViewModel
import com.example.rappitest.viewmodel.RappiTestViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieSearchViewModel::class)
    abstract fun bindMovieSearchViewModel(viewModel: MovieSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    abstract fun bindMovieViewModel(viewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvShowSearchViewModel::class)
    abstract fun bindTvShowSearchViewModel(viewModel: TvShowSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvShowViewModel::class)
    abstract fun bindTvShowViewModel(viewModel: TvShowViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: RappiTestViewModelFactory): ViewModelProvider.Factory
}
