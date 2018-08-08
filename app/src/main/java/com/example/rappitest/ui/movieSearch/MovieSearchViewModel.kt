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
        nextPageHandler.queryNextPage(getCategory())
    }

    fun refresh() {
        categoryIdItemPosition.value?.let {
            categoryIdItemPosition.value = it
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val repository: MovieRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var category: MovieRepository.Category? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(category: MovieRepository.Category) {
            if (this.category == category) {
                return
            }
            unregister()
            this.category = category
            nextPageLiveData = repository.searchNextPage(category)
            loadMoreState.value = LoadMoreState(
                    isRunning = true,
                    errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                category = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }
    }
}
