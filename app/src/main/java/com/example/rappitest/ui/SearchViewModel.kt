package com.example.rappitest.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.rappitest.repository.PaginateRepository
import com.example.rappitest.repository.TvShowRepository
import com.example.rappitest.ui.tvShowSearch.TvShowSearchViewModel
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.Status


/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */

abstract class SearchViewModel<T> : ViewModel() {

    abstract fun request(): LiveData<Resource<List<T>>>

    var isMovie: Boolean = true

    var categoryIdItemPosition = MutableLiveData<Int>()


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

    class NextPageHandler(private val repository: PaginateRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var category: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(category: String) {
            Log.d("TvShowSearchViewModel", "queryNextPage")
            if (this.category == category) {
                return
            }
            Log.d("TvShowSearchViewModel", "queryNextPage after")
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
            Log.d("TvShowSearchViewModel", "onChanged")
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        Log.d("TvShowSearchViewModel", "_hasMore: $_hasMore")
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
            Log.d("TvShowSearchViewModel", "unregister")
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
