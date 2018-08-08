package com.example.rappitest.ui.movieSearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.rappitest.repository.MovieRepository
import com.example.rappitest.ui.SearchViewModel
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.Status
import com.example.rappitest.vo.movie.Movie
import java.util.Locale
import javax.inject.Inject

//@OpenForTesting
class MovieSearchViewModel @Inject constructor(private val movieRepository: MovieRepository)
    : SearchViewModel<Movie>() {

    private val nextPageHandler = NextPageHandler(movieRepository)

    val results: LiveData<Resource<List<Movie>>> = Transformations
            .switchMap(categoryIdItemPosition) { request() }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    init {
        isMovie = true
    }

    fun getCategory(): MovieRepository.Category {
        return when (categoryIdItemPosition.value) {
            0 -> MovieRepository.Category.MOVIE_UPCOMING
            1 -> MovieRepository.Category.MOVIE_POPULAR
            else -> MovieRepository.Category.MOVIE_TOP_RATED
        }
    }

    override fun request(): LiveData<Resource<List<Movie>>> {
        nextPageHandler.reset()
        return movieRepository.search(getCategory())
    }

    fun loadNextPage() {
        Log.d("MovieSearchViewModel", "loadNextPage")
        nextPageHandler.queryNextPage(getCategory().name)
    }

    fun refresh() {
        categoryIdItemPosition.value?.let {
            categoryIdItemPosition.value = it
        }
    }
}
