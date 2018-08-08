package com.example.rappitest.ui.tvShowSearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.example.rappitest.repository.TvShowRepository
import com.example.rappitest.ui.SearchViewModel
import com.example.rappitest.vo.Resource
import com.example.rappitest.vo.tv.TvShow
import javax.inject.Inject

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 06/08/2018.
 */
class TvShowSearchViewModel @Inject constructor(private val tvShowRepository: TvShowRepository)
    : SearchViewModel<TvShow>() {

    private val nextPageHandler = NextPageHandler(tvShowRepository)

    val results: LiveData<Resource<List<TvShow>>> = Transformations
            .switchMap(categoryIdItemPosition) { search ->
                tvShowRepository.search(getCategory())
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

    override fun request() : LiveData<Resource<List<TvShow>>> {
        nextPageHandler.reset()
        return tvShowRepository.search(getCategory())
    }

    fun loadNextPage() {
        Log.d("TvShowSearchViewModel", "loadNextPage")
        nextPageHandler.queryNextPage(getCategory().name)
    }

    fun refresh() {
        categoryIdItemPosition.value?.let {
            categoryIdItemPosition.value = it
        }
    }
}