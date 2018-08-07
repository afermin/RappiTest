package com.example.rappitest.ui.tvShowSearch

import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import com.example.rappitest.repository.TvShowRepository
import com.example.rappitest.ui.SearchViewModel
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.Status
import com.example.rappitest.vo.tv.TvShow
import java.util.*
import javax.inject.Inject

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */
class TvShowSearchViewModel @Inject constructor(movieRepository: TvShowRepository)
    : SearchViewModel() {

    private val nextPageHandler = NextPageHandler(movieRepository)

    val results: LiveData<Resource<List<TvShow>>> = Transformations
            .switchMap(categoryIdItemPosition) { search ->
                movieRepository.search(getCategory())

            }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    init {
        isMovie = false
    }

    fun getCategory() : TvShowRepository.Category {
        return when (categoryIdItemPosition.value) {
            0-> TvShowRepository.Category.TV_SHOW_POPULAR
            else -> TvShowRepository.Category.TV_SHOW_TOP_RATED
        }
    }

    override fun query() {

    }

    fun loadNextPage() {
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

    class NextPageHandler(private val repository: TvShowRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var category: TvShowRepository.Category? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(category: TvShowRepository.Category) {
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